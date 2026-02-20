package com.voxcom.vox.system;

import org.json.JSONObject;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;

public class SpeechRecognizer {

    private static Model model;

    static {
        try {
            model = new Model("models/vosk-model");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String listen() {

        try {
            Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

            System.out.println("\nAvailable recording devices:");
            int chosenIndex = -1;

            for (int i = 0; i < mixerInfos.length; i++) {

                Mixer mixer = AudioSystem.getMixer(mixerInfos[i]);

                Line.Info[] targetLines = mixer.getTargetLineInfo(
                        new DataLine.Info(TargetDataLine.class, null));

                if (targetLines.length > 0) {
                    System.out.println(i + " -> " + mixerInfos[i].getName());

                    String name = mixerInfos[i].getName().toLowerCase();

                    // Prefer real mic, avoid "port"
                    if (chosenIndex == -1 &&
                            name.contains("microphone") &&
                            !name.contains("port")) {

                        chosenIndex = i;
                    }
                }
            }

            if (chosenIndex == -1)
                throw new RuntimeException("No real microphone found");

            System.out.println("Using device: " + mixerInfos[chosenIndex].getName());

            Mixer mixer = AudioSystem.getMixer(mixerInfos[chosenIndex]);

            // open in native format (safe)
            AudioFormat baseFormat = new AudioFormat(48000, 16, 1, true, false);
            TargetDataLine mic = (TargetDataLine) mixer.getLine(new DataLine.Info(TargetDataLine.class, baseFormat));
            mic.open(baseFormat);
            mic.start();

            // ---------- CONVERT TO VOSK FORMAT ----------
            AudioFormat voskFormat = new AudioFormat(16000, 16, 1, true, false);
            AudioInputStream converted = AudioSystem.getAudioInputStream(voskFormat, new AudioInputStream(mic));

            Recognizer recognizer = new Recognizer(model, 16000);

            byte[] buffer = new byte[4096];
            long start = System.currentTimeMillis();

            System.out.println("Speak command...");

            while (System.currentTimeMillis() - start < 6000) {

                int n = converted.read(buffer);
                if (n <= 0)
                    break;

                if (recognizer.acceptWaveForm(buffer, n)) {
                    return new JSONObject(recognizer.getResult()).getString("text");
                }
            }

            return new JSONObject(recognizer.getFinalResult()).getString("text");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}