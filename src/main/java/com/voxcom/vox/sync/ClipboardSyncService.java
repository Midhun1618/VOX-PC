package com.voxcom.vox.sync;

import com.google.cloud.firestore.*;
import com.voxcom.vox.FirebaseService;
import com.voxcom.vox.ui.layouts.ProfileSidebar;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class ClipboardSyncService {

    private static boolean internalChange = false;
    private static DocumentReference stateRef;
    private static ProfileSidebar sidebar;

    public static void start(String uid, ProfileSidebar ui) {

        sidebar = ui;

        stateRef = FirebaseService.getDB()
                .collection("users")
                .document(uid)
                .collection("clipboard")
                .document("state");

        listenForRemoteChanges();
    }

    // PC → Firestore
    public static void sendFromPC(String text) {

        if(internalChange) return;

        try {
            stateRef.set(new ClipboardState(text, "desktop", System.currentTimeMillis()));

            if(sidebar != null)
                sidebar.updateClipboardText(text);

        } catch (Exception ignored) {}
    }

    // Firestore → PC
    private static void listenForRemoteChanges() {

        stateRef.addSnapshotListener((snap, err) -> {

            if(err != null || snap == null || !snap.exists()) return;

            String text = snap.getString("content");
            String device = snap.getString("device");

            if(text == null) return;

            if("desktop".equals(device)) return;

            internalChange = true;
            copyToClipboard(text);
            internalChange = false;

            if(sidebar != null)
                sidebar.updateClipboardText(text);
        });
    }

    private static void copyToClipboard(String text) {
        StringSelection sel = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
    }

    static class ClipboardState {
        public String content;
        public String device;
        public long timestamp;

        public ClipboardState(String c, String d, long t) {
            content = c;
            device = d;
            timestamp = t;
        }
    }
}
