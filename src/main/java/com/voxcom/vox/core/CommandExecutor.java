package com.voxcom.vox.core;

import java.awt.Desktop;
import java.net.URI;

public class CommandExecutor {

    public static void execute(String command) {

        command = command.toLowerCase();

        try {

            if (command.contains("open youtube")) {
                Desktop.getDesktop().browse(new URI("https://youtube.com"));
            }

            else if (command.contains("open google")) {
                Desktop.getDesktop().browse(new URI("https://google.com"));
            }

            else if (command.contains("exit vox")) {
                System.exit(0);
            }

            else {
                System.out.println("Unknown command: " + command);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}