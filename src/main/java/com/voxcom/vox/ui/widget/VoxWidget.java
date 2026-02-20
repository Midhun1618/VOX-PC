package com.voxcom.vox.ui.widget;

import javax.swing.*;
import java.awt.*;

public class VoxWidget {

    private final JWindow window;
    private final JLabel iconLabel;

    public VoxWidget() {
        window = new JWindow();
        window.setAlwaysOnTop(true);
        window.setBackground(new Color(0,0,0,0));
        window.setSize(120,120);

        // bottom right
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(screen.width - 150, screen.height - 200);

        iconLabel = new JLabel();
        window.add(iconLabel);

        setState("idle");
        window.setVisible(true);
    }

    public void setState(String state) {
        SwingUtilities.invokeLater(() -> {
            String path = switch (state) {
                case "detected" -> "widget_states/wake.png";
                case "listening" -> "widget_states/listening.png";
                case "processing" -> "widget_states/processing.png";
                default -> "widget_states/idle.png";
            };

            iconLabel.setIcon(new ImageIcon(path));
        });
    }
    public void close() {
        SwingUtilities.invokeLater(window::dispose);
    }
}