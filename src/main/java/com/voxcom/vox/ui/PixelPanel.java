package com.voxcom.vox.ui;

import javax.swing.*;
import java.awt.*;

public class PixelPanel extends JPanel {

    public PixelPanel() {
        setBackground(VoxTheme.PANEL);
        setBorder(BorderFactory.createLineBorder(VoxTheme.YELLOW, 2));
    }
}
