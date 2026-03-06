
package com.voxcom.vox.config;

import java.io.*;
import java.util.Properties;

public class VoxSettings {

    private static final String DIR =
            System.getProperty("user.home") + File.separator + ".vox";
    private static final String FILE = DIR + File.separator + "settings.properties";

    private static final Properties props = new Properties();

    static { load(); }

    public static synchronized void load() {
        try {
            File folder = new File(DIR);
            if (!folder.exists()) folder.mkdirs();

            File file = new File(FILE);
            if (!file.exists()) file.createNewFile();

            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void save(String key, String value) {
        try (FileOutputStream fos = new FileOutputStream(FILE)) {
            props.setProperty(key, value);
            props.store(fos, "VOX Settings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized boolean getBoolean(String key) {
        return "true".equalsIgnoreCase(props.getProperty(key));
    }
    public static boolean isAssistantEnabled() {
    if (props.getProperty("assistant.enabled") == null) {
        save("assistant.enabled", "true");
        return true;
    }
    return getBoolean("assistant.enabled");
}
}