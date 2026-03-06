package com.voxcom.vox.ui.frame;

import com.voxcom.vox.system.ClipboardWatcher;
import com.voxcom.vox.SessionManager;
import com.voxcom.vox.sync.ClipboardSyncService;
import com.voxcom.vox.ui.layouts.LeftSidebar;
import com.voxcom.vox.ui.layouts.ProfileSidebar;
import com.voxcom.vox.ui.screens.*;
import com.voxcom.vox.ui.theme.VoxTheme;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private static DashboardFrame instance;

    private JPanel centerContainer;
    private ProfileSidebar profileSidebar;
    private String uid;

    public DashboardFrame(String uid, String email, String username, int avatarIndex) {

        instance = this;
        this.uid = uid;

        setTitle(username + " | VOX");
        setSize(900, 700);
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/voxicon.png"));
        setIconImage(icon);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(VoxTheme.BG);

        centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(VoxTheme.BG);

        profileSidebar = new ProfileSidebar(username, email, avatarIndex);

        // NAVIGATION PANEL
        LeftSidebar sidebar = new LeftSidebar(
                username,
                email,
                this::showHome,
                this::showSettings,
                this::logout);

        // Combine both panels
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(profileSidebar, BorderLayout.NORTH);
        leftPanel.add(sidebar, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(centerContainer, BorderLayout.CENTER);

        showHome();

        // 🚀 START VOX SERVICES
        startClipboardServices();

    }

    private void startClipboardServices() {

        ClipboardSyncService.start(uid, profileSidebar);

        new Thread(ClipboardWatcher::new).start();
    }

    public void showHome() {
        setScreen(new HomeScreen(uid));
    }

    public void showSettings() {
        setScreen(new SettingsScreen());
    }

    private void logout() {
        dispose();
        SessionManager.clear();
        new LoginFrame().setVisible(true);

    }

    private void setScreen(JPanel panel) {
        centerContainer.removeAll();
        centerContainer.add(panel, BorderLayout.CENTER);
        centerContainer.revalidate();
        centerContainer.repaint();
    }

    public void bringToFront() {

        if (!isVisible()) {
            setVisible(true);
        }
        setExtendedState(JFrame.NORMAL);
        toFront();
        requestFocus();
    }

    public static DashboardFrame getInstance() {
        return instance;
    }

}
