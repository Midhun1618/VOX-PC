package com.voxcom.vox.ui.screens;

import javax.swing.*;
import java.awt.*;

public class HistoryScreen extends JPanel {

    public HistoryScreen() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        add(label("Completed task"));
        add(label("Missed workout"));
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.GRAY);
        l.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return l;
    }
}