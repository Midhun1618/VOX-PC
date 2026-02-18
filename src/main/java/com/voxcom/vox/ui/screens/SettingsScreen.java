package com.voxcom.vox.ui.screens;

import javax.swing.*;
import java.awt.*;

public class SettingsScreen extends JPanel {

    public SettingsScreen() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel label = new JLabel("SETTINGS PANEL (Coming Soon)");
        label.setForeground(Color.CYAN);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        add(label, BorderLayout.CENTER);
    }
}
