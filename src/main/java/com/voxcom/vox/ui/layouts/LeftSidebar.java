package com.voxcom.vox.ui.layouts;

import com.voxcom.vox.ui.theme.VoxTheme;
import com.voxcom.vox.ui.theme.PixelButton;

import javax.swing.*;
import java.awt.*;

public class LeftSidebar extends JPanel {

    private ProfileSidebar profile;

    public LeftSidebar(String name, String email,
                       Runnable onHome,
                       Runnable onSettings,
                       Runnable onLogout) {

        setPreferredSize(new Dimension(230, 0));
        setLayout(new BorderLayout());
        setBackground(VoxTheme.PANEL);


        // BOTTOM → NAV BUTTONS
        add(createButtons(onHome, onSettings, onLogout), BorderLayout.SOUTH);
    }

    private JPanel createButtons(Runnable onHome, Runnable onSettings, Runnable onLogout) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(VoxTheme.PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(20,15,20,15));

        PixelButton home = new PixelButton("HOME");
        PixelButton settings = new PixelButton("SETTINGS");
        PixelButton logout = new PixelButton("LOGOUT");

        home.setAlignmentX(Component.CENTER_ALIGNMENT);
        settings.setAlignmentX(Component.CENTER_ALIGNMENT);
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);

        home.addActionListener(e -> onHome.run());
        settings.addActionListener(e -> onSettings.run());
        logout.addActionListener(e -> onLogout.run());

        panel.add(home);
        panel.add(Box.createVerticalStrut(10));
        panel.add(settings);
        panel.add(Box.createVerticalStrut(20));
        panel.add(logout);

        return panel;
    }

    // expose stats update methods
    public ProfileSidebar stats() {
        return profile;
    }
}
