package com.voxcom.vox.ui;

import javax.swing.*;

public class PixelTextField extends JTextField {

    public PixelTextField() {
        setBackground(VoxTheme.BG);
        setForeground(VoxTheme.YELLOW);
        setCaretColor(VoxTheme.YELLOW);
        setFont(FontUtil.PIXEL_12);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(VoxTheme.YELLOW, 2),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
    }
}
