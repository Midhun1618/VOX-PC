package com.voxcom.vox.ui.screens;

import com.voxcom.vox.ui.layouts.TopTabs;
import com.voxcom.vox.ui.theme.VoxTheme;

import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JPanel {

    private final String uid;

    private JPanel content;

    public HomeScreen(String uid) {

        this.uid = uid;

        setLayout(new BorderLayout());
        setBackground(VoxTheme.BG);

        content = new JPanel(new BorderLayout());
        content.setBackground(VoxTheme.BG);

        add(new TopTabs(
                this::showTasks,
                this::showHistory,
                this::showReminders
        ), BorderLayout.NORTH);

        add(content, BorderLayout.CENTER);

        showTasks();
    }


    private void set(JPanel panel) {
        content.removeAll();
        content.add(panel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    private void showTasks() { set(new TasksScreen(uid)); }
    private void showHistory() { set(new HistoryScreen(uid)); }

    private void showReminders() { set(new RemindersScreen()); }
}
