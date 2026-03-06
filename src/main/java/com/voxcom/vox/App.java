package com.voxcom.vox;

import com.voxcom.vox.ui.frame.DashboardFrame;
import com.voxcom.vox.ui.frame.LoginFrame;
import com.voxcom.vox.core.VoxBackground;
import com.voxcom.vox.config.VoxSettings;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {

        FirebaseService.getDB();

        SwingUtilities.invokeLater(() -> {

            if (SessionManager.isLoggedIn()) {

                new DashboardFrame(
                        SessionManager.getUID(),
                        SessionManager.getEmail(),
                        SessionManager.getUsername(),
                        SessionManager.getAvatarIndex()).setVisible(true);

            } else {
                new LoginFrame().setVisible(true);
            }

        });
        if (VoxSettings.isAssistantEnabled()) {

            new Thread(() -> {
                try {
                    VoxBackground.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }
    }
}
