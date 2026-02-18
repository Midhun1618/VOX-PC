package com.voxcom.vox;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.InputStream;

public class FirebaseService {

    private static Firestore db;

    public static Firestore getDB() {

        if (db != null) return db;

        try {
            InputStream serviceAccount =
                    FirebaseService.class.getClassLoader()
                            .getResourceAsStream("serviceAccountKey.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();

            System.out.println("Firestore connected");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return db;
    }
}
