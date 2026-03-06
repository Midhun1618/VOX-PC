package com.voxcom.vox.system;

import javax.sound.sampled.*;
import java.io.File;

public class AudioRecorder {

    public static File record(int seconds) throws Exception {

        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        TargetDataLine line = AudioSystem.getTargetDataLine(format);
        line.open(format);
        line.start();

        File wav = File.createTempFile("vox_record", ".wav");
        wav.deleteOnExit();

        AudioInputStream ais = new AudioInputStream(line);

        new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000);
                line.stop();
                line.close();
            } catch (Exception ignored) {}
        }).start();

        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wav);

        return wav;
    }
}