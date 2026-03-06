package com.voxcom.vox.core;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.voxcom.vox.App;
import com.voxcom.vox.App;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class CommandExecutor {

    public static void execute(String command) {

        command = command.toLowerCase();

        try {

            if (command.contains("open youtube")) {
                Desktop.getDesktop().browse(new URI("https://youtube.com"));
            } else if (command.contains("open dashboard")) {
                if (App.dashboard != null) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        App.dashboard.bringToFront();
                    });
                }
                return;
            } else if (command.contains("minimize dashboard")) {

                if (App.dashboard != null) {

                    SwingUtilities.invokeLater(() -> {
                        App.dashboard.setState(JFrame.ICONIFIED);
                    });

                }

                return;
            } else if (command.contains("show settings panel")) {

                if (App.dashboard != null) {

                    SwingUtilities.invokeLater(() -> {
                        App.dashboard.bringToFront();
                        App.dashboard.showSettings();
                    });

                }

                return;
            } else if (command.contains("show task panel")) {

                if (App.dashboard != null) {

                    SwingUtilities.invokeLater(() -> {
                        App.dashboard.bringToFront();
                        App.dashboard.showHome();
                    });

                }

            }
            if (command.startsWith("search ")||command.startsWith("ask google")) {
                String query = "";
                try {
                    if(command.contains("ask google")){
                        query = command.replaceFirst("search ", "").trim();
                    }else if(command.contains("search")){
                        query = command.replaceFirst("search ", "").trim();
                    }
                   

                    if (!query.isEmpty()) {
                        if(command.contains("in youtube")){
                            String queryNew = query.replaceFirst("in youtube", "").trim();
                            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
                            String url = "https://www.youtube.com/search?q=" + encoded;
                            Desktop.getDesktop().browse(new URI(url));
                        }else{
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