package com.voxcom.vox.core;

import com.voxcom.vox.config.VoxSettings;
import com.voxcom.vox.system.WakewordService;
import com.voxcom.vox.ui.widget.VoxWidget;

public class VoxBackground {

    private static WakewordService wakeService;
    private static VoxWidget widget;

    public static void main(String[] args) throws Exception {

        System.out.println("VOX background started");

        CommandServer.start();

        while (true) {

            boolean enabled = VoxSettings.getBoolean("voice");

            if (enabled && wakeService == null) {
                System.out.println("Starting VOX assistant...");
                widget = new VoxWidget();
                wakeService = new WakewordService(widget);
            }

            if (!enabled && wakeService != null) {
                System.out.println("Stopping VOX assistant...");
                wakeService.stop();
                widget.close();
                wakeService = null;
                widget = null;
            }

            Thread.sleep(1000);
        }
    }
}