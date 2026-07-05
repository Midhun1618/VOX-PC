package com.voxcom.vox.ui.widget;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;

public class VoxWidget {

    private final JWindow window;
    private final ImagePanel mainPanel;

    private static final int TARGET_SIZE = 50;
    private static final int TOP_PADDING = 20; // Added top padding from the screen edge

    // State Paths
    private static final String PATH_IDLE = "/widget_states/state_idle.png";
    private static final String PATH_ACTIVATE = "/widget_states/state_activate.png";
    private static final String PATH_ACTIVATE2 = "/widget_states/state_activate2.png";
    private static final String PATH_ACTIVATE3 = "/widget_states/state_activate3.png";
    private static final String PATH_OFFLINE = "/widget_states/state_offline.png";
    private static final String PATH_ERROR = "/widget_states/state_error.png";

    // Cached Images
    private BufferedImage imgIdle;
    private BufferedImage imgActivate;
    private BufferedImage imgActivate2;
    private BufferedImage imgActivate3;
    private BufferedImage imgOffline;
    private BufferedImage imgError;

    private Timer scaleTimer;
    private Timer rotationTimer;
    private Timer errorTimer;

    private double currentScale = 1.0;
    private double currentRotationDegrees = 0.0;
    private int currentXOffset = 0; 
    private BufferedImage currentImage;

    public VoxWidget() {
        preloadImages();

        window = new JWindow();
        window.setBackground(new Color(0, 0, 0, 0));
        window.setAlwaysOnTop(true);
        window.setFocusableWindowState(false);

        mainPanel = new ImagePanel();
        window.setContentPane(mainPanel);
        window.setSize(TARGET_SIZE, TARGET_SIZE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - TARGET_SIZE) / 2;
        window.setLocation(x, TOP_PADDING);

        setState("idle");
        window.setVisible(true);
        window.toFront();
    }

    public synchronized void setState(String state) {
        stopScaleAnimation();
        stopListeningAnimation();
        stopErrorAnimation();
        
        currentScale = 1.0;
        currentRotationDegrees = 0.0;
        currentXOffset = 0;
        updateWindowLocation();

        if (state == null) {
            state = "idle";
        }

        switch (state) {
            case "detected" -> {
                startScaleUpAnimation();
            }
            case "listening" -> {
                currentImage = imgActivate;
                startListeningAnimation();
            }
            case "processing" -> {
                currentImage = imgOffline;
                mainPanel.repaint();
            }
            case "offline" -> {
                currentImage = imgError;
                mainPanel.repaint();
            }
            case "error" -> {
                currentImage = imgError;
                startErrorAnimation();
            }
            default -> {
                startScaleDownToIdleAnimation();
            }
        }
    }

    public void close() {
        stopScaleAnimation();
        stopListeningAnimation();
        stopErrorAnimation();
        window.setVisible(false);
        window.dispose();
    }

    private void preloadImages() {
        imgIdle = convertToBufferedImage(loadRawImage(PATH_IDLE));
        imgActivate = convertToBufferedImage(loadRawImage(PATH_ACTIVATE));
        imgActivate2 = convertToBufferedImage(loadRawImage(PATH_ACTIVATE2));
        imgActivate3 = convertToBufferedImage(loadRawImage(PATH_ACTIVATE3));
        imgOffline = convertToBufferedImage(loadRawImage(PATH_OFFLINE));
        imgError = convertToBufferedImage(loadRawImage(PATH_ERROR));
    }

    private Image loadRawImage(String path) {
        URL url = getClass().getResource(path);
        if (url == null) {
            return new BufferedImage(TARGET_SIZE, TARGET_SIZE, BufferedImage.TYPE_INT_ARGB);
        }
        return new ImageIcon(url).getImage();
    }

    private BufferedImage convertToBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bi = new BufferedImage(TARGET_SIZE, TARGET_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, TARGET_SIZE, TARGET_SIZE, null);
        g2d.dispose();
        return bi;
    }

    private void updateWindowLocation() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int baseX = (screen.width - TARGET_SIZE) / 2;
        window.setLocation(baseX + currentXOffset, TOP_PADDING);
    }

    private void startScaleUpAnimation() {
        currentScale = 0.1;
        currentImage = imgActivate3;
        mainPanel.repaint();

        final long duration = 200; // 500ms total scale up transition
        final long startTime = System.currentTimeMillis();

        scaleTimer = new Timer(15, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = (float) elapsed / duration;

            if (progress >= 1.0f) {
                progress = 1.0f;
                scaleTimer.stop();
            }

            currentScale = 0.1 + (0.9 * progress);

            // Change image sequential framing based on progress intervals
            if (progress < 0.33f) {
                currentImage = imgActivate3;
            } else if (progress < 0.66f) {
                currentImage = imgActivate2;
            } else {
                currentImage = imgActivate;
            }

            mainPanel.repaint();
        });
        scaleTimer.start();
    }

    private void startScaleDownToIdleAnimation() {
        currentScale = 1.0;
        currentImage = imgActivate;
        mainPanel.repaint();

        final long duration = 300; // 500ms total down scale transition
        final long startTime = System.currentTimeMillis();

        scaleTimer = new Timer(15, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = (float) elapsed / duration;

            if (progress >= 1.0f) {
                progress = 1.0f;
                scaleTimer.stop();
            }

            currentScale = 1.0 - (0.9 * progress);

            // Reverse order image progressive assignment
            if (progress < 0.33f) {
                currentImage = imgActivate;
            } else if (progress < 0.66f) {
                currentImage = imgActivate2;
            } else if (progress < 0.95f) {
                currentImage = imgActivate3;
            } else {
                currentImage = imgIdle;
            }

            mainPanel.repaint();
        });
        scaleTimer.start();
    }

    private void stopScaleAnimation() {
        if (scaleTimer != null && scaleTimer.isRunning()) {
            scaleTimer.stop();
        }
    }

    private void startListeningAnimation() {
        final long transitionDuration = 200;
        final long holdDuration = 1200;
        final long startTime = System.currentTimeMillis();

        rotationTimer = new Timer(15, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                long cycleLength = (transitionDuration + holdDuration) * 2;
                long cycleTime = elapsed % cycleLength;

                if (cycleTime < transitionDuration) {
                    double t = (double) cycleTime / transitionDuration;
                    currentRotationDegrees = 20.0 - (40.0 * t);
                } else if (cycleTime < transitionDuration + holdDuration) {
                    currentRotationDegrees = -20.0;
                } else if (cycleTime < (transitionDuration * 2) + holdDuration) {
                    double t = (double) (cycleTime - (transitionDuration + holdDuration)) / transitionDuration;
                    currentRotationDegrees = -20.0 + (40.0 * t);
                } else {
                    currentRotationDegrees = 20.0;
                }

                mainPanel.repaint();
            }
        });
        rotationTimer.start();
    }

    private void stopListeningAnimation() {
        if (rotationTimer != null && rotationTimer.isRunning()) {
            rotationTimer.stop();
        }
    }

    private void startErrorAnimation() {
        final long startTime = System.currentTimeMillis();
        final int maxShakeOffset = 8; // Max horizontal variance boundary in pixels

        errorTimer = new Timer(15, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            
            // Vigorous high-frequency mathematical swinging on the X-axis
            double frequency = 0.05; 
            currentXOffset = (int) (maxShakeOffset * Math.sin(elapsed * frequency));
            
            updateWindowLocation();
            mainPanel.repaint();
        });
        errorTimer.start();
    }

    private void stopErrorAnimation() {
        if (errorTimer != null && errorTimer.isRunning()) {
            errorTimer.stop();
        }
    }

    private class ImagePanel extends JPanel {
        
        public ImagePanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentImage == null) return;

            Graphics2D g2 = (Graphics2D) g.create();
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int w = getWidth();
            int h = getHeight();
            double centerX = w / 2.0;
            double centerY = h / 2.0;

            AffineTransform tx = new AffineTransform();
            tx.translate(centerX, centerY);
            
            if (currentScale != 1.0) {
                tx.scale(currentScale, currentScale);
            }
            
            if (currentRotationDegrees != 0.0) {
                tx.rotate(Math.toRadians(currentRotationDegrees));
            }
            
            tx.translate(-currentImage.getWidth() / 2.0, -currentImage.getHeight() / 2.0);

            g2.drawImage(currentImage, tx, null);
            g2.dispose();
        }
    }
}