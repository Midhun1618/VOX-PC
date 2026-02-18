package com.voxcom.vox.ui.screens;

import com.voxcom.vox.ui.theme.PixelButton;
import com.voxcom.vox.ui.theme.PixelTextField;
import com.voxcom.vox.ui.theme.VoxTheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RemindersScreen extends JPanel {

    private final JPanel listPanel;
    private final List<String> reminders = new ArrayList<>();

    public RemindersScreen() {

        setLayout(new BorderLayout());
        setBackground(VoxTheme.BG);

        // TOP INPUT BAR
        JPanel top = new JPanel(new BorderLayout(8,0));
        top.setBackground(VoxTheme.BG);
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        PixelTextField field = new PixelTextField();
        PixelButton addBtn = new PixelButton("REMIND");

        addBtn.addActionListener(e -> {
            String text = field.getText().trim();
            if(text.isEmpty()) return;

            reminders.add(text);
            field.setText("");
            refresh();
        });

        top.add(field, BorderLayout.CENTER);
        top.add(addBtn, BorderLayout.EAST);

        // LIST PANEL
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(VoxTheme.BG);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(VoxTheme.BG);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        reminders.add("Meeting at 6 PM");
        reminders.add("Drink water");
        refresh();
    }

    private void refresh() {
        listPanel.removeAll();

        for(String t : reminders) {
            JLabel lbl = new JLabel(t);
            lbl.setForeground(Color.CYAN);
            lbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            listPanel.add(lbl);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}
