package com.voxcom.vox.ui.layouts;

import com.voxcom.vox.ui.theme.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ProfileSidebar extends JPanel {

    private JLabel addedLbl = new JLabel("ADDED: 0");
    private JLabel doneLbl = new JLabel("DONE: 0");
    private JLabel missedLbl = new JLabel("MISSED: 0");
    private JLabel focusLbl = new JLabel("FOCUS: 0%");
    private JLabel clipboardLbl = new JLabel();

    private int added = 0;
    private int done = 0;
    private int missed = 0;
    private int avatarIndex;


    public ProfileSidebar(String name, String email, int avatarIndex) {

        this.avatarIndex = avatarIndex;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(VoxTheme.PANEL);
        setBorder(BorderFactory.createEmptyBorder(20,15,20,15));

        JLabel nameLbl = new JLabel(name);
        nameLbl.setForeground(VoxTheme.YELLOW);
        nameLbl.setFont(FontUtil.PIXEL_16);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLbl = new JLabel(email);
        emailLbl.setForeground(VoxTheme.YELLOW_DIM);
        emailLbl.setFont(FontUtil.PIXEL_12);
        emailLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(createAvatar()); 

        add(Box.createVerticalStrut(10));
        add(nameLbl);
        add(emailLbl);

        add(Box.createVerticalStrut(25));
        add(createStatsPanel());

        add(Box.createVerticalStrut(20));
        setupClipboardLabel();
        add(clipboardLbl);
    }


    private JPanel createStatsPanel() {

        JPanel stats = new JPanel();
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
        stats.setBackground(VoxTheme.PANEL);

        style(addedLbl);
        style(doneLbl);
        style(missedLbl);
        style(focusLbl);

        stats.add(addedLbl);
        stats.add(doneLbl);
        stats.add(missedLbl);
        stats.add(Box.createVerticalStrut(10));
        stats.add(focusLbl);

        return stats;
    }

    private void setupClipboardLabel() {
        clipboardLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        clipboardLbl.setForeground(VoxTheme.YELLOW_DIM);
        clipboardLbl.setFont(FontUtil.PIXEL_12);
        clipboardLbl.setMaximumSize(new Dimension(180, 200));
        updateClipboardText("empty");
    }

    private void style(JLabel lbl) {
        lbl.setForeground(VoxTheme.YELLOW);
        lbl.setFont(FontUtil.PIXEL_12);
    }

    private void updateFocus() {
        int total = done + missed;
        int focus = total == 0 ? 0 : (int)((done * 100.0f) / total);
        focusLbl.setText("FOCUS: " + focus + "%");
    }

    public void updateClipboardText(String text) {

        if(text == null || text.isEmpty())
            text = "empty";

        if(text.length() > 140)
            text = text.substring(0,140) + "...";

        clipboardLbl.setText(
                "<html><b>Clipboard</b><br><div style='width:160px'>" +
                        text.replace("\n","<br>") +
                        "</div></html>"
        );
    }

    public void taskAdded() {
        added++;
        addedLbl.setText("ADDED: " + added);
    }

    public void taskDone() {
        done++;
        doneLbl.setText("DONE: " + done);
        updateFocus();
    }

    public void taskMissed() {
        missed++;
        missedLbl.setText("MISSED: " + missed);
        updateFocus();
    }

    private JLabel createAvatar() {

    JLabel avatar = new JLabel();
    avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

    try {

        String path = "/avatars/avatar" + (avatarIndex+1) + ".png";
        System.out.println(path);
        URL url = getClass().getResource(path);

        if(url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(72,72,Image.SCALE_SMOOTH);
            avatar.setIcon(new ImageIcon(img));
        } else {
            avatar.setText("No Img");
            System.out.println("Avatar not found: " + path);
        }

    } catch (Exception e) {
        avatar.setText("No Img");
        e.printStackTrace();
    }

    return avatar;
}


}
