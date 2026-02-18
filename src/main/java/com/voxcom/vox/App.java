package com.voxcom.vox;

import com.voxcom.vox.ui.frame.DashboardFrame;
import com.voxcom.vox.ui.frame.LoginFrame;

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
    }
}
