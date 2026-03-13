package com.voxcom.vox.core;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.voxcom.vox.App;
import com.voxcom.vox.ui.frame.DashboardFrame;
import com.voxcom.vox.voice.VoxTTS;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class CommandExecutor {

    public static void execute(String command) {

        command = command.toLowerCase();
        DashboardFrame dash = DashboardFrame.getInstance();

        try {

            if (command.contains("open youtube")) {
                VoxTTS.speak("Opening YouTube sir, please check my text to speech feature now, is it okay for our purpose");
                Desktop.getDesktop().browse(new URI("https://youtube.com"));
                
            } else if (command.contains("open dashboard")) {
                if (dash != null) {
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

            } else if (command.startsWith("search ") || command.startsWith("ask google")) {
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
                            String encoded = URLEncoder.encode(queryNew, StandardCharsets.UTF_8);
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
                    for (int i = 0; i < 10; i++) {
                        Runtime.getRuntime().exec(
                                "powershell -command (new-object -com wscript.shell).SendKeys([char]175)");
                    }

                    System.out.println("Volume increased");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            } else if (command.contains("decrease volume") || command.contains("volume down")) {

                try {

                    for (int i = 0; i < 10; i++) {
                        Runtime.getRuntime().exec(
                                "powershell -command (new-object -com wscript.shell).SendKeys([char]174)");
                    }

                    System.out.println("Volume increased");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            } else if (command.contains("mute volume") || command.contains("mute")) {

                try {

                    Runtime.getRuntime().exec(
                            "powershell -command (new-object -com wscript.shell).SendKeys([char]173)");

                    System.out.println("Volume muted");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            } else if (command.contains("maximum volume") || command.contains("max volume")) {

                try {

                    for (int i = 0; i <= 10; i++) {
                        Runtime.getRuntime().exec(
                                "powershell -command (new-object -com wscript.shell).SendKeys([char]175)");
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
            } else if (command.contains("tell the time") || command.contains("what time")) {

                LocalTime time = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

                System.out.println("Current time is: " + time.format(formatter));

                return;
            } else if (command.contains("open mail") || command.contains("open gmail")) {

                Desktop.getDesktop().browse(new URI("https://mail.google.com"));

                return;
            } else if (command.contains("open github")) {

                Desktop.getDesktop().browse(new URI("https://github.com"));

                return;
            } else if (command.contains("open settings")) {

                Runtime.getRuntime().exec("start ms-settings:");

                return;
            } else if (command.contains("increase brightness")) {

                Runtime.getRuntime().exec(
                        "powershell (Get-WmiObject -Namespace root/WMI -Class WmiMonitorBrightnessMethods).WmiSetBrightness(1,80)");

                System.out.println("Brightness increased");

                return;
            } else if (command.contains("decrease brightness")) {

                Runtime.getRuntime().exec(
                        "powershell (Get-WmiObject -Namespace root/WMI -Class WmiMonitorBrightnessMethods).WmiSetBrightness(1,30)");

                System.out.println("Brightness decreased");

                return;
            } else if (command.contains("turn on bluetooth") || command.contains("enable bluetooth")) {

                Runtime.getRuntime().exec(new String[] {
                        "powershell",
                        "-Command",
                        "Get-PnpDevice | Where-Object {$_.FriendlyName -like '*Bluetooth*' -and $_.Status -eq 'Disabled'} | Enable-PnpDevice -Confirm:$false"
                });

                System.out.println("Bluetooth turned on");
                return;
            } else if (command.contains("turn off bluetooth") || command.contains("disable bluetooth")) {

                Runtime.getRuntime().exec(new String[] {
                        "powershell",
                        "-Command",
                        "Get-PnpDevice | Where-Object {$_.FriendlyName -like '*Bluetooth*' -and $_.Status -eq 'OK'} | Disable-PnpDevice -Confirm:$false"
                });

                System.out.println("Bluetooth turned off");
                return;
            } else if (command.contains("game time") || (command.contains("start") && command.contains("meeting"))
                    || command.contains("call boys")) {

                Desktop.getDesktop().browse(new URI("https://meet.google.com/new"));
                Thread.sleep(5000);
                Runtime.getRuntime().exec("powershell -command (new-object -com wscript.shell).SendKeys('^l')");
                Thread.sleep(500);
                Runtime.getRuntime().exec("powershell -command (new-object -com wscript.shell).SendKeys('^c')");
                
                Runtime.getRuntime().exec("powershell -command \"(new-object -com wscript.shell).SendKeys('{ESC}')");
                Runtime.getRuntime().exec("powershell -command \"(new-object -com wscript.shell).SendKeys('{ESC}')");

                return;
            }

            else {
                System.out.println("Unknown command: " + command);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}