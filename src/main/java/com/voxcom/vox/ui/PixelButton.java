package com.voxcom.vox.ui;

import javax.swing.*;
import java.awt.*;

public class PixelButton extends JButton {

    public PixelButton(String text) {
        super(text);
        setFont(FontUtil.PIXEL_12);
        setBackground(VoxTheme.YELLOW);
        setForeground(Color.BLACK);
        setFocusPainted(false);
        setBorder(BorderFactory.createLineBorder(VoxTheme.YELLOW, 2));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

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
