package com.voxcom.vox.voice;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class VoxTTS {

    private static Voice voice;

    static {

        try {

            System.setProperty(
                    "freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory"
            );

            VoiceManager vm = VoiceManager.getInstance();
            voice = vm.getVoice("kevin16");

            if (voice == null) {
                System.err.println("FreeTTS voice not found.");
            } else {
                voice.allocate();
                voice.setRate(170);
                voice.setPitch(250);
                voice.setVolume(1.0f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void speak(String text) {

        if (voice == null) {
            System.err.println("Voice not initialized.");
            return;
        }

        new Thread(() -> voice.speak(text)).start();
    }

    public static void shutdown() {
        if (voice != null) {
            voice.deallocate();
        }
    }
}