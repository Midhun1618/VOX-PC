package com.voxcom.vox.core;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import com.voxcom.vox.App;
import com.voxcom.vox.ui.frame.DashboardFrame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class CommandExecutor {

    private static final int KEY_VOLUME_UP = 0xAF;
    private static final int KEY_VOLUME_DOWN = 0xAE;
    private static final int KEY_VOLUME_MUTE = 0xAD;

    public static void execute(String command) {

        command = command.toLowerCase();
        DashboardFrame dash = DashboardFrame.getInstance();

        try {

            if (command.contains("open youtube")) {
                Desktop.getDesktop().browse(new URI("https://youtube.com"));
            } else if (command.contains("open dashboard")) {
                if (dash!= null) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        dash.bringToFront();
                    });
                }
                return;
            } else if (command.contains("minimize dashboard")) {

                if (dash != null) {

                    SwingUtilities.invokeLater(() -> {
                        dash.setState(JFrame.ICONIFIED);
                    });

                }

                return;
            } else if (command.contains("show settings panel")) {

                if (dash != null) {

                    SwingUtilities.invokeLater(() -> {
                        dash.bringToFront();
                        dash.showSettings();
                    });

                }

                return;
            } else if (command.contains("show task panel")) {

                if (dash != null) {

                    SwingUtilities.invokeLater(() -> {
                        dash.bringToFront();
                        dash.showHome();
                    });

                }

            }
            else if (command.startsWith("search ") || command.startsWith("ask google")) {
                String query = "";
                try {
                    if (command.contains("ask google")) {
                        query = command.replaceFirst("search ", "").trim();
                    } else if (command.contains("search")) {
                        query = command.replaceFirst("search ", "").trim();
                    }

                    if (!query.isEmpty()) {
                        if (command.contains("in youtube")) {
                            String queryNew = query.replaceFirst("in youtube", "").trim();
                            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
                            String url = "https://www.youtube.com/search?q=" + encoded;
                            Desktop.getDesktop().browse(new URI(url));
                        } else {
                            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
                            String url = "https://www.google.com/search?q=" + encoded;
                            Desktop.getDesktop().browse(new URI(url));
                            System.out.println("Searching Google for: " + query);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            } else if (command.contains("increase volume") || command.contains("volume up")) {

                try {

                    Robot robot = new Robot();

                    for (int i = 0; i < 10; i++) {
                        robot.keyPress(KEY_VOLUME_UP);
                        robot.keyRelease(KEY_VOLUME_UP);
                    }

                    System.out.println("Volume increased");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            } else if (command.contains("decrease volume") || command.contains("volume down")) {

                try {

                    Robot robot = new Robot();

                    for (int i = 0; i < 10; i++) {
                        robot.keyPress(KEY_VOLUME_DOWN);
                        robot.keyRelease(KEY_VOLUME_DOWN);
                    }

                    System.out.println("Volume increased");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }
            else if (command.contains("mute volume") || command.contains("mute")) {

                try {

                    Robot robot = new Robot();

                    robot.keyPress(KEY_VOLUME_MUTE);
                    robot.keyRelease(KEY_VOLUME_MUTE);

                    System.out.println("Volume muted");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }
            else if (command.contains("maximum volume") || command.contains("max volume")) {

                try {

                    Robot robot = new Robot();

                    for (int i = 0; i <= 10; i++) {
                        robot.keyPress(KEY_VOLUME_UP);
                        robot.keyRelease(KEY_VOLUME_UP);
                    }

                    System.out.println("Volume max");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }

            else if (command.contains("open google")) {
                Desktop.getDesktop().browse(new URI("https://google.com"));
            }

            else if (command.contains("shut down")) {
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