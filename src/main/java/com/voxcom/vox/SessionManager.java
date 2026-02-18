package com.voxcom.vox;

import java.util.prefs.Preferences;

public class SessionManager {

    private static final Preferences prefs = Preferences.userRoot().node("vox_session");

    public static void save(String uid, String email, String username, int avatarIndex) {
        prefs.put("uid", uid);
        prefs.put("email", email);
        prefs.put("username", username);
        prefs.putInt("avatarIndex", avatarIndex);
        System.out.println(avatarIndex);
        prefs.putBoolean("loggedIn", true);
    }

    public static int getAvatarIndex() {
        return prefs.getInt("avatarIndex", 1);
    }

    public static boolean isLoggedIn() {
        return prefs.getBoolean("loggedIn", false);
    }

    public static String getUID() {
        return prefs.get("uid", null);
    }

    public static String getEmail() {
        return prefs.get("email", null);
    }

    public static String getUsername() {
        return prefs.get("username", "User");
    }

    public static void clear() {
        prefs.remove("uid");
        prefs.remove("email");
        prefs.remove("username");
        prefs.putBoolean("loggedIn", false);
    }
}
