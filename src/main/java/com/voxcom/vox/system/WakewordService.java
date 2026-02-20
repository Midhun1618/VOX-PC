package com.voxcom.vox.system;

import ai.picovoice.porcupine.*;
import com.voxcom.vox.core.CommandExecutor;
import com.voxcom.vox.ui.widget.VoxWidget;

import javax.sound.sampled.*;
import java.io.*;

public class WakewordService {

    private static final String ACCESS_KEY = "l4YcMaXwFVLjkElTdruR5vz2fjZ3Vwd0CuGnfDR/lg0ifYd/iQzgmA==";
    private boolean running = true;
    private final VoxWidget widget;

    public WakewordService(VoxWidget widget) {
        this.widget = widget;
        start();
    }

    // ⭐ Converts resource → real file for native engine
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

        new Thread(() -> {

            Porcupine porcupine = null;
            TargetDataLine mic = null;

            try {

                // ⭐ IMPORTANT FIX
                String keywordPath = extractResource("/wakeword/vox.ppn");
                String modelPath = extractResource("/wakeword/porcupine_params.pv");

                porcupine = new Porcupine.Builder()
                        .setAccessKey(ACCESS_KEY)
                        .setKeywordPath(keywordPath)
                        .setModelPath(modelPath)
                        .build();

                AudioFormat format = new AudioFormat(
                        porcupine.getSampleRate(),
                        16,
                        1,
                        true,
                        false
                );

                mic = AudioSystem.getTargetDataLine(format);
                mic.open(format);
                mic.start();

                int frameLength = porcupine.getFrameLength();
                byte[] buffer = new byte[frameLength * 2];
                short[] pcm = new short[frameLength];

                System.out.println("VOX listening for wake word...");

                while (running) {

                    mic.read(buffer, 0, buffer.length);

                    for (int i = 0; i < frameLength; i++) {
                        pcm[i] = (short)((buffer[i*2] & 0xff) | (buffer[i*2+1] << 8));
                    }

                    int result = porcupine.process(pcm);

                    if (result >= 0) {
                        onWakeWord();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mic != null) mic.close();
                if (porcupine != null) porcupine.delete();
            }

        }).start();
    }

    private void onWakeWord() {

        System.out.println("Wake word detected!");

        widget.setState("detected");

        try { Thread.sleep(600); } catch (Exception ignored) {}

        widget.setState("listening");

        String command = SpeechRecognizer.listen();

        widget.setState("processing");

        CommandExecutor.execute(command);

        widget.setState("idle");
    }

    public void stop() {
        running = false;
    }
}