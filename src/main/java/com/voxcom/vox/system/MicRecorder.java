package com.voxcom.vox.system;

import javax.sound.sampled.*;
import java.io.File;

public class MicRecorder {

    public static File record(int seconds) throws Exception {

        System.out.println("\nAvailable microphones:");

        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixers.length; i++) {
            Mixer mixer = AudioSystem.getMixer(mixers[i]);
            if (mixer.getTargetLineInfo().length > 0) {
                System.out.println(i + " -> " + mixers[i].getName());
            }
        }

        AudioFormat micFormat = getSupportedFormat();

        // ⭐ use REAL microphone instead of default
        TargetDataLine mic = getRealMic(micFormat);
        mic.start();

        System.out.println("Recording...");

        File raw = File.createTempFile("vox_raw", ".wav");
        raw.deleteOnExit();

        AudioInputStream micStream = new AudioInputStream(mic);

        // stop after N seconds
        new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000);
                mic.stop();
                mic.close();
            } catch (Exception ignored) {}
        }).start();

        AudioSystem.write(micStream, AudioFileFormat.Type.WAVE, raw);

        System.out.println("Converting audio for Whisper...");

        // Convert to 16k mono
        AudioInputStream source = AudioSystem.getAudioInputStream(raw);

        AudioFormat whisperFormat = new AudioFormat(
                16000,
                16,
                1,
                true,
                false
        );

        AudioInputStream converted =
                AudioSystem.getAudioInputStream(whisperFormat, source);

        File whisperFile = File.createTempFile("vox_clean", ".wav");
        whisperFile.deleteOnExit();

        AudioSystem.write(converted, AudioFileFormat.Type.WAVE, whisperFile);

        return whisperFile;
    }


    // ---------- PICK REAL MICROPHONE ----------
    private static TargetDataLine getRealMic(AudioFormat format) throws Exception {

        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

        for (Mixer.Info info : mixers) {

            String name = info.getName().toLowerCase();

            // ignore fake/loopback devices
            if (name.contains("speaker") ||
                name.contains("stereo") ||
                name.contains("primary") ||
                name.contains("port"))
                continue;

            Mixer mixer = AudioSystem.getMixer(info);

            for (Line.Info lineInfo : mixer.getTargetLineInfo()) {

                if (lineInfo instanceof DataLine.Info dataLine) {

                    try {
                        TargetDataLine line =
                                (TargetDataLine) mixer.getLine(dataLine);
                        line.open(format);

                        System.out.println("Using microphone: " + info.getName());
                        return line;

                    } catch (Exception ignored) {}
                }
            }
        }

        throw new RuntimeException("No usable microphone found");
    }


    private static AudioFormat getSupportedFormat() {

        AudioFormat[] formats = new AudioFormat[] {
                new AudioFormat(48000,16,1,true,false),
                new AudioFormat(44100,16,1,true,false),
                new AudioFormat(44100,16,2,true,false),
                new AudioFormat(48000,16,2,true,false)
        };

        for (AudioFormat format : formats) {
            try {
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                if (AudioSystem.isLineSupported(info))
                    return format;
            } catch (Exception ignored) {}
        }

        return new AudioFormat(44100,16,1,true,false);
    }
}