package com.voxcom.vox.ui.screens;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SettingsScreen extends JPanel {

    public SettingsScreen() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel title = new JLabel("VOX SETTINGS");
        title.setForeground(Color.CYAN);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        add(title, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Feature Toggles
        mainPanel.add(createToggle("Clipboard Sync"));
        mainPanel.add(createToggle("Voice Command"));
        mainPanel.add(createToggle("Talkback"));
        mainPanel.add(createToggle("Reminder Notification"));

        mainPanel.add(Box.createVerticalStrut(30));

        // Directory Slots
        mainPanel.add(createPathSelector("VS Code Path"));
        mainPanel.add(createPathSelector("Android Studio Path"));
        mainPanel.add(createPathSelector("Manual App Path"));

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
    }

    private JPanel createToggle(String name) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel label = new JLabel(name);
        label.setForeground(Color.WHITE);

        JToggleButton toggle = new JToggleButton("OFF");
        toggle.setFocusPainted(false);
        toggle.setBackground(Color.DARK_GRAY);
        toggle.setForeground(Color.RED);

        toggle.addActionListener(e -> {
            if (toggle.isSelected()) {
                toggle.setText("ON");
                toggle.setForeground(Color.GREEN);
            } else {
                toggle.setText("OFF");
                toggle.setForeground(Color.RED);
            }
        });

        panel.add(label, BorderLayout.WEST);
        panel.add(toggle, BorderLayout.EAST);

        return panel;
    }

    private JPanel createPathSelector(String labelName) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.BLACK);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel label = new JLabel(labelName);
        label.setForeground(Color.WHITE);

        JTextField pathField = new JTextField();
        pathField.setBackground(Color.DARK_GRAY);
        pathField.setForeground(Color.WHITE);

        JButton browseBtn = new JButton("Browse");
        browseBtn.setBackground(Color.GRAY);
        browseBtn.setForeground(Color.BLACK);

        browseBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                pathField.setText(selectedFile.getAbsolutePath());
            }
        });

        JPanel rightPanel = new JPanel(new BorderLayout(5, 0));
        rightPanel.setBackground(Color.BLACK);
        rightPanel.add(pathField, BorderLayout.CENTER);
        rightPanel.add(browseBtn, BorderLayout.EAST);

        panel.add(label, BorderLayout.NORTH);
        panel.add(rightPanel, BorderLayout.CENTER);

        return panel;
    }
}