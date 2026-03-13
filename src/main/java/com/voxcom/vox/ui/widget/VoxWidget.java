package com.voxcom.vox.ui.widget;

import javax.swing.*;
import java.awt.*;

public class VoxWidget {

    private final JWindow window;
    private final JLabel iconLabel;

    private int idleX;
    private int activeX;
    private int y;

    private Timer listeningAnimation;
    private int frameIndex = 0;

    private final String[] listeningFrames = {
            "/widget_states/state_thinking01.png",
            "/widget_states/state_thinking02.png",
            "/widget_states/state_thinking03.png",
            "/widget_states/state_activate.png",
            "/widget_states/state_thinking03.png",
            "/widget_states/state_thinking02.png"
    };

    public VoxWidget() {

        window = new JWindow();

        window.setBackground(new Color(0, 0, 0, 0));
        window.setAlwaysOnTop(true);
        window.setFocusableWindowState(false);

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
        window.setSize(50, 50);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        activeX = screen.width - 60;
        idleX = screen.width - 20;
        y = screen.height - 120;

        window.setLocation(idleX, y);

        setState("idle");

        window.setVisible(true);
        window.toFront();
    }

    public void setState(String state) {

        stopListeningAnimation();

        if (state.equals("listening")) {
            slideTo(activeX); 
            startListeningAnimation();
            return;
        }

        String path = switch (state) {
            case "detected" -> "/widget_states/state_activate.png";
            case "processing" -> "/widget_states/state_offline.png";
            case "offline" -> "/widget_states/state_error.png";
            default -> "/widget_states/state_idle.png";
        };

        iconLabel.setIcon(loadIcon(path, 50));

        if (state.equals("idle"))
            slideTo(idleX);
        else
            slideTo(activeX);
        window.repaint();
    }

    private void slideTo(int targetX) {

        new Thread(() -> {

            int currentX = window.getX();

            while (Math.abs(currentX - targetX) > 2) {

                if (currentX < targetX)
                    currentX += 2;
                else
                    currentX -= 2;

                window.setLocation(currentX, y);

                try {
                    Thread.sleep(5);
                } catch (Exception ignored) {
                }
            }

            window.setLocation(targetX, y);

        }).start();
    }

    public void close() {
        window.setVisible(false);
        window.dispose();
    }

    private ImageIcon loadIcon(String path, int size) {

        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);

        return new ImageIcon(img);
    }

    private void startListeningAnimation() {

        if (listeningAnimation != null && listeningAnimation.isRunning())
            return;

        listeningAnimation = new Timer(200, e -> {

            iconLabel.setIcon(loadIcon(listeningFrames[frameIndex], 50));

            frameIndex++;
            if (frameIndex >= listeningFrames.length)
                frameIndex = 0;

        });

        listeningAnimation.start();
    }

    private void stopListeningAnimation() {

        if (listeningAnimation != null) {
            listeningAnimation.stop();
            frameIndex = 0;
        }
    }
}