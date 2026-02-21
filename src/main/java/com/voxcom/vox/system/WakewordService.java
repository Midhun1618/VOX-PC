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

    // Extract resource file to real temp file (required for native lib)
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
                        porcupine.getSampleRate(), 16, 1, true, false
                );

                mic = AudioSystem.getTargetDataLine(format);
                mic.open(format);
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
                        pcm[i] = (short)((buffer[i*2] & 0xff) | (buffer[i*2+1] << 8));

                    if (porcupine.process(pcm) >= 0) {
                        handleWake();
                    }
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

            try {

                System.out.println("Wake word detected!");
                widget.setState("detected");

                Thread.sleep(350);

                // STOP PORCUPINE MIC
                mic.stop();
                mic.close();

                widget.setState("listening");

                String command = SpeechRecognizer.listen();

                widget.setState("processing");
                CommandExecutor.execute(command);

                // RESTART WAKE MIC
                AudioFormat format = new AudioFormat(
                        porcupine.getSampleRate(), 16, 1, true, false
                );

                mic = AudioSystem.getTargetDataLine(format);
                mic.open(format);
                mic.start();

                widget.setState("idle");

            } catch (Exception e) {
                e.printStackTrace();
            }

            busy = false;

        }).start();
    }

    public void stop() {
        running = false;
        try {
            if (mic != null) mic.close();
            if (porcupine != null) porcupine.delete();
        } catch (Exception ignored) {}
    }
}