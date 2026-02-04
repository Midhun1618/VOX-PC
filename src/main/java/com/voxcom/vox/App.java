package com.voxcom.vox;


import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (SessionManager.isLoggedIn()) {
                new DashboardFrame(
                        SessionManager.getUid(),
                        SessionManager.getEmail(),
                        SessionManager.getUsername()
                ).setVisible(true);
            } else {
                new LoginFrame().setVisible(true);
            }
        });
    }
}
