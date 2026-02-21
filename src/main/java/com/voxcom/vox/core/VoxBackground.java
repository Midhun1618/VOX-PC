package com.voxcom.vox.core;

import com.voxcom.vox.config.VoxSettings;
import com.voxcom.vox.system.WakewordService;
import com.voxcom.vox.ui.widget.VoxWidget;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

public class VoxBackground {

    private static WakewordService wakeService;
    private static VoxWidget widget;

    public static void main(String[] args) throws Exception {

        System.out.println("VOX background started");
        CommandServer.start();

        while (true) {

            boolean enabled = VoxSettings.getBoolean("voice");

            // ---------- START ASSISTANT ----------
            if (enabled && wakeService == null) {

                System.out.println("Starting VOX assistant...");

                CountDownLatch latch = new CountDownLatch(1);

                // create widget on Swing thread and WAIT
                SwingUtilities.invokeLater(() -> {
                    widget = new VoxWidget();
                    latch.countDown();
                });

                latch.await(); // IMPORTANT: wait until widget exists

                wakeService = new WakewordService(widget);
            }

            // ---------- STOP ASSISTANT ----------
            if (!enabled && wakeService != null) {

                System.out.println("Stopping VOX assistant...");

                wakeService.stop();

                if (widget != null) {
                    widget.close();
                    widget = null;
                }

                wakeService = null;
            }

            Thread.sleep(1000);
        }
    }
}