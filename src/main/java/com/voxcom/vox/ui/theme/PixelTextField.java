package com.voxcom.vox.ui.theme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PixelTextField extends JTextField {

    public PixelTextField() {

        setBackground(VoxTheme.BG);
        setForeground(VoxTheme.YELLOW);
        setCaretColor(VoxTheme.YELLOW);

        setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(VoxTheme.YELLOW,2),
                new EmptyBorder(5,5,5,5)
        ));
    }
}
