package com.voxcom.vox.ui.theme;

import javax.swing.*;
import java.awt.*;

public class PixelButton extends JButton {

    public PixelButton(String text) {
        super(text);

        setFocusPainted(false);
        setBackground(VoxTheme.YELLOW);
        setForeground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(VoxTheme.YELLOW, 2));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(VoxTheme.YELLOW_DIM);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(VoxTheme.YELLOW);
            }
        });
    }
}
