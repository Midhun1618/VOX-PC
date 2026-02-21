package com.voxcom.vox.ui.widget;

import javax.swing.*;
import java.awt.*;

public class VoxWidget {

    private final JWindow window;
    private final JLabel iconLabel;

    public VoxWidget() {

        window = new JWindow();

        // IMPORTANT: real transparency support
        window.setBackground(new Color(0,0,0,0));
        window.setAlwaysOnTop(true);
        window.setFocusableWindowState(false);

        // panel that actually paints
        JPanel panel = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setComposite(AlphaComposite.Src);
            }
        };

        panel.setOpaque(false);

        iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(iconLabel, BorderLayout.CENTER);

        window.setContentPane(panel);
        window.setSize(60,60);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(screen.width-80, screen.height-120);

        setState("idle");

        window.setVisible(true);
        window.toFront();
    }

    public void setState(String state) {

        String path = switch (state) {
            case "detected" -> "/widget_states/state_wake.png";
            case "listening" -> "/widget_states/state_listen.png";
            case "processing" -> "/widget_states/state_process.png";
            default -> "/widget_states/state_idle.png";
        };

        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        iconLabel.setIcon(icon);

        window.repaint();
    }

    public void close() {
        window.setVisible(false);
        window.dispose();
    }
}