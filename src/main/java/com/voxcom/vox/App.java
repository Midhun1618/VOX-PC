package com.voxcom.vox;


import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("VOX PC App Started");
            new LoginFrame().setVisible(true);
        });
    }
}