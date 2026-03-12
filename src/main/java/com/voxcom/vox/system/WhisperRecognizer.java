package com.voxcom.vox.system;

import java.io.*;

public class WhisperRecognizer {

    private static final String MODEL = "whisper/model/ggml-tiny.en-q5_1.bin";

    private static final String EXECUTABLE = "whisper/whisper-cli.exe";

    public static String recognize(File wavFile) {

        try {

            ProcessBuilder builder = new ProcessBuilder(
                    EXECUTABLE,
                    "-m", MODEL,
                    "-f", wavFile.getAbsolutePath(),
                    "-nt"
            );

            builder.redirectErrorStream(true);

            Process process = builder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            StringBuilder fullOutput = new StringBuilder();

            String transcript = "";

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()
                        || line.startsWith("whisper_")
                        || line.startsWith("system_info")
                        || line.startsWith("main:")
                        || line.startsWith("load")
                        || line.startsWith("encode")
                        || line.startsWith("decode")
                        || line.startsWith("batchd")
                        || line.startsWith("prompt")
                        || line.startsWith("total")) {
                    continue;
                }

                transcript = line;
            }

            process.waitFor();

            String text = fullOutput.toString().trim();

            text = transcript.replaceAll("\\[.*?\\]", "").trim();

            return text.toLowerCase();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}