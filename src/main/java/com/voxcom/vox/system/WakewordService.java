package com.voxcom.vox.system;

import ai.picovoice.porcupine.*;
import com.voxcom.vox.core.CommandExecutor;
import com.voxcom.vox.ui.widget.VoxWidget;

import javax.sound.sampled.*;
import java.io.*;

public class WakewordService {

    private static final String ACCESS_KEY = "l4YcMaXwFVLjkElTdruR5vz2fjZ3Vwd0CuGnfDR/lg0ifYd/iQzgmA==";

    private volatile boolean running = true;
    private volatile boolean busy = false;

    private Porcupine porcupine;
    private TargetDataLine mic;
    private Thread wakeThread;

    private final VoxWidget widget;

    public WakewordService(VoxWidget widget) {
        this.widget = widget;
        start();
    }

    private String extractResource(String path) throws Exception {

        InputStream in = getClass().getResourceAsStream(path);
        if (in == null)
            throw new RuntimeException("Missing resource: " + path);

        File temp = File.createTempFile("vox_", "_" + new File(path).getName());
        temp.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(temp)) {
            in.transferTo(out);
        }

        return temp.getAbsolutePath();
    }

    private TargetDataLine getRealMic(AudioFormat format) throws Exception {

        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

        for (Mixer.Info info : mixers) {

            String name = info.getName().toLowerCase();

            // skip fake/loopback devices
            if (name.contains("speaker") ||
                name.contains("stereo") ||
                name.contains("primary") ||
                name.contains("port"))
                continue;

            Mixer mixer = AudioSystem.getMixer(info);

            for (Line.Info lineInfo : mixer.getTargetLineInfo()) {

                if (lineInfo instanceof DataLine.Info dataLine) {
                    try {
                        TargetDataLine line = (TargetDataLine) mixer.getLine(dataLine);
                        line.open(format);

                        System.out.println("Wake mic: " + info.getName());
                        return line;

                    } catch (Exception ignored) {}
                }
            }
        }

        throw new RuntimeException("No usable microphone found");
    }

    private void start() {

        wakeThread = new Thread(() -> {

            try {

                String keywordPath = extractResource("/wakeword/vox.ppn");
                String modelPath = extractResource("/wakeword/porcupine_params.pv");

                porcupine = new Porcupine.Builder()
                        .setAccessKey(ACCESS_KEY)
                        .setKeywordPath(keywordPath)
                        .setModelPath(modelPath)
                        .build();

                AudioFormat format = new AudioFormat(
                        porcupine.getSampleRate(), 16, 1, true, false);

                mic = getRealMic(format);
                mic.start();

                int frameLength = porcupine.getFrameLength();
                byte[] buffer = new byte[frameLength * 2];
                short[] pcm = new short[frameLength];

                System.out.println("VOX listening for wake word...");

                while (running) {

                    if (busy) {
                        Thread.sleep(80);
                        continue;
                    }

                    mic.read(buffer, 0, buffer.length);

                    for (int i = 0; i < frameLength; i++)
                        pcm[i] = (short) ((buffer[i * 2] & 0xff) | (buffer[i * 2 + 1] << 8));

                    if (porcupine.process(pcm) >= 0)
                        handleWake();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        wakeThread.start();
    }

    private void handleWake() {

        if (busy) return;
        busy = true;

        new Thread(() -> {

            File audio = null;

            try {

                System.out.println("Wake word detected!");
                SoundPlayer.play("/sounds/sfx_hello.wav");
                widget.setState("detected");

                Thread.sleep(350);
                mic.stop();
                mic.close();

                widget.setState("listening");

                audio = MicRecorder.record(4);

                String command = WhisperRecognizer.recognize(audio);

                System.out.println("Heard: " + command);

                if (command == null || command.isBlank()) {
                    widget.setState("offline");
                    SoundPlayer.play("/sounds/sfx_sorry.wav");
                    Thread.sleep(1500);
                    return;
                }

                widget.setState("processing");
                SoundPlayer.play("/sounds/sfx_done.wav");

                CommandExecutor.execute(command);

            } catch (Exception e) {
                e.printStackTrace();
                widget.setState("offline");
            } finally {

                try {
                    restartWakeMic();
                } catch (Exception ignored) {}

                widget.setState("idle");
                busy = false;
            }

        }).start();
    }

    private void restartWakeMic() throws Exception {

        AudioFormat format = new AudioFormat(
                porcupine.getSampleRate(), 16, 1, true, false);

        mic = getRealMic(format);
        mic.start();
    }

    public void stop() {
        running = false;
        try {
            if (mic != null) mic.close();
            if (porcupine != null) porcupine.delete();
        } catch (Exception ignored) {}
    }
}