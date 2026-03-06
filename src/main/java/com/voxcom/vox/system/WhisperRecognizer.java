package com.voxcom.vox.system;

import java.io.*;

public class WhisperRecognizer {

    private static final String MODEL =
            "whisper/model/ggml-base.en.bin";

    private static final String EXECUTABLE =
            "whisper/whisper-cli.exe";

    public static String recognize(File wavFile) {

        try {

            ProcessBuilder builder = new ProcessBuilder(
                    EXECUTABLE,
                    "-m", MODEL,
                    "-f", wavFile.getAbsolutePath(),
                    "-nt"   // no timestamps
            );

            builder.redirectErrorStream(true);

            Process process = builder.start();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

            String line;
            StringBuilder fullOutput = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                System.out.println("WHISPER RAW: " + line);
                fullOutput.append(line).append(" ");
            }

            process.waitFor();

            String text = fullOutput.toString().trim();

            text = text.replaceAll("\\[.*?\\]", "").trim();

            return text.toLowerCase();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}