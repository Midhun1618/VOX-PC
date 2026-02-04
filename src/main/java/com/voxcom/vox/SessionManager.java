package com.voxcom.vox;

import java.util.prefs.Preferences;

public class SessionManager {

    private static final Preferences prefs =
            Preferences.userRoot().node("VOX");

    public static void saveSession(String uid, String email, String username) {
        prefs.put("uid", uid);
        prefs.put("email", email);
        prefs.put("username", username);
    }

    public static boolean isLoggedIn() {
        return prefs.get("uid", null) != null;
    }

    public static String getUid() {
        return prefs.get("uid", null);
    }

    public static String getEmail() {
        return prefs.get("email", null);
    }

    public static String getUsername() {
        return prefs.get("username", null);
    }

    public static void clear() {
        prefs.remove("uid");
        prefs.remove("email");
        prefs.remove("username");
    }
}
