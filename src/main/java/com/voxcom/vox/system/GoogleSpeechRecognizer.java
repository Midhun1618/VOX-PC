package com.voxcom.vox.system;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import java.io.File;
import java.nio.file.Files;

public class GoogleSpeechRecognizer {

    public static String recognize(File audioFile) {

        try (SpeechClient client = SpeechClient.create()) {

            byte[] data = Files.readAllBytes(audioFile.toPath());
            ByteString audioBytes = ByteString.copyFrom(data);

            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("en-US")
                            .build();

            RecognitionAudio audio =
                    RecognitionAudio.newBuilder()
                            .setContent(audioBytes)
                            .build();

            RecognizeResponse response = client.recognize(config, audio);

            for (SpeechRecognitionResult result : response.getResultsList())
                return result.getAlternativesList().get(0).getTranscript();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}