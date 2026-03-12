package com.voxcom.vox.core;

import com.voxcom.vox.config.VoxSettings;
import com.voxcom.vox.system.WakewordService;
import com.voxcom.vox.ui.widget.VoxWidget;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

public class VoxBackground {

    private static WakewordService wakeService;
    private static VoxWidget widget;

    private static boolean running = false;

    public static void start() {

        if (running) return;
        running = true;

        new Thread(() -> {

            try {

                System.out.println("VOX background started");
                CommandServer.start();

                while (running) {

                    boolean enabled = VoxSettings.getBoolean("voice");

                    if (enabled && wakeService == null) {

                        System.out.println("Starting VOX assistant...");

                        CountDownLatch latch = new CountDownLatch(1);

                        SwingUtilities.invokeLater(() -> {
                            widget = new VoxWidget();
                            latch.countDown();
                        });

                        latch.await();

                        wakeService = new WakewordService(widget);
                    }

                    if (!enabled && wakeService != null) {

                        System.out.println("Stopping VOX assistant");
                        wakeService.stop();

                        if (widget != null) {
                            widget.close();
                            widget = null;
                        }

                        wakeService = null;
                    }

                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static void stop() {
        running = false;
    }

    public static void main(String[] args) {
        start();
    }
}