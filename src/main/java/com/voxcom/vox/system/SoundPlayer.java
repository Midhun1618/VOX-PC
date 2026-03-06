package com.voxcom.vox.system;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundPlayer {

    public static void play(String resourcePath) {

        new Thread(() -> {
            try {
                InputStream audioSrc = SoundPlayer.class.getResourceAsStream(resourcePath);
                if (audioSrc == null) {
                    System.out.println("Missing sound: " + resourcePath);
                    return;
                }

                BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream stream = AudioSystem.getAudioInputStream(bufferedIn);

                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clip.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}