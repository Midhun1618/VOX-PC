package com.voxcom.vox;

import com.google.cloud.firestore.*;
import com.google.cloud.Timestamp;
import com.voxcom.vox.ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class DashboardFrame extends JFrame {

    private final Firestore db;
    private final String uid;
    private final String email;
    private final String username;

    private JPanel listPanel;
    private JLabel statsLabel;
    private Timer refreshTimer;

    public DashboardFrame(String uid, String email, String username) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.db = FirebaseService.getDB();

        setTitle(username + " | VOX");
        setSize(760, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(VoxTheme.BG);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createLeftPanel(), BorderLayout.WEST);
        add(createTaskPanel(), BorderLayout.CENTER);

        loadTasks();
        loadStats();
        startAutoRefresh();

        setIconImage(
            new ImageIcon(
                getClass().getResource("/icons/voxicon.png")
            ).getImage()
        );
    }

    /* ───────────── TOP PROFILE BAR ───────────── */

    private JPanel createTopPanel() {
        PixelPanel top = new PixelPanel();
        top.setLayout(new BorderLayout(12, 0));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel avatar = new JLabel();
        avatar.setPreferredSize(new Dimension(64, 64));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(VoxTheme.PANEL);

        JLabel nameLbl = new JLabel(username);
        nameLbl.setFont(FontUtil.PIXEL_16);
        nameLbl.setForeground(VoxTheme.YELLOW);

        JLabel emailLbl = new JLabel(email);
        emailLbl.setFont(FontUtil.PIXEL_12);
        emailLbl.setForeground(VoxTheme.YELLOW_DIM);

        info.add(nameLbl);
        info.add(emailLbl);

        statsLabel = new JLabel("Loading stats...");
        statsLabel.setFont(FontUtil.PIXEL_12);
        statsLabel.setForeground(VoxTheme.YELLOW);

        top.add(avatar, BorderLayout.WEST);
        top.add(info, BorderLayout.CENTER);
        top.add(statsLabel, BorderLayout.EAST);

        loadAvatar(avatar);
        return top;
    }

    private void loadAvatar(JLabel avatarLabel) {
        new Thread(() -> {
            try {
                DocumentSnapshot doc =
                        db.collection("users").document(uid).get().get();

                int index = doc.contains("avatarIndex")
                        ? doc.getLong("avatarIndex").intValue()
                        : 0;

                ImageIcon icon = new ImageIcon(
                        getClass().getResource("/avatars/avatar" + (index + 1) + ".png")
                );

                Image scaled =
                        icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);

                SwingUtilities.invokeLater(() ->
                        avatarLabel.setIcon(new ImageIcon(scaled))
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /* ───────────── LEFT PANEL (TIME + LOGOUT) ───────────── */

    private JPanel createLeftPanel() {
        PixelPanel left = new PixelPanel();
        left.setPreferredSize(new Dimension(180, 0));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel timeLabel = new JLabel();
        timeLabel.setFont(FontUtil.PIXEL_16);
        timeLabel.setForeground(VoxTheme.YELLOW);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        PixelButton logout = new PixelButton("LOGOUT");
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);

        logout.addActionListener(e -> {
            SessionManager.clear();
            dispose();
            new LoginFrame().setVisible(true);
        });

        startClock(timeLabel);

        left.add(Box.createVerticalStrut(40));
        left.add(timeLabel);
        left.add(Box.createVerticalStrut(40));
        left.add(logout);

        return left;
    }

    /* ───────────── TASK PANEL ───────────── */

    private JPanel createTaskPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(VoxTheme.BG);

        JPanel addRow = new JPanel(new BorderLayout(8, 0));
        addRow.setBackground(VoxTheme.BG);
        addRow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        PixelTextField taskField = new PixelTextField();
        PixelButton addBtn = new PixelButton("ADD");

        addBtn.addActionListener(e -> {
            String text = taskField.getText().trim();
            if (text.isEmpty()) return;

            Map<String, Object> task = Map.of(
                "title", text,
                "completed", false,
                "createdAt", Timestamp.now(),
                "expiresAt",
                Timestamp.ofTimeSecondsAndNanos(
                        Timestamp.now().getSeconds() + 86400, 0
                )
            );

            db.collection("users")
              .document(uid)
              .collection("tasks")
              .add(task);

            db.collection("users")
              .document(uid)
              .update("totalTasks", FieldValue.increment(1));

            taskField.setText("");
            refreshTasks();
            loadStats();
        });

        addRow.add(taskField, BorderLayout.CENTER);
        addRow.add(addBtn, BorderLayout.EAST);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(VoxTheme.BG);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(VoxTheme.BG);

        container.add(addRow, BorderLayout.NORTH);
        container.add(scroll, BorderLayout.CENTER);
        return container;
    }

    /* ───────────── TASK LOADING ───────────── */

    private void loadTasks() {
        try {
            CollectionReference tasks =
                    db.collection("users")
                      .document(uid)
                      .collection("tasks");

            List<QueryDocumentSnapshot> docs =
                    tasks.whereEqualTo("completed", false)
                         .get()
                         .get()
                         .getDocuments();

            for (QueryDocumentSnapshot doc : docs) {
                TaskData task = doc.toObject(TaskData.class);
                if (task == null || task.title == null) continue;

                listPanel.add(createTaskRow(tasks, doc.getId(), task));
            }

            revalidate();
            repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createTaskRow(
            CollectionReference tasks,
            String taskId,
            TaskData task
    ) {
        PixelPanel row = new PixelPanel();
        row.setLayout(new BorderLayout());
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel lbl = new JLabel(task.title);
        lbl.setFont(FontUtil.PIXEL_12);
        lbl.setForeground(VoxTheme.YELLOW);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        PixelButton done = new PixelButton("✔");
        done.addActionListener(e -> {
            tasks.document(taskId).update("completed", true);
            db.collection("users")
              .document(uid)
              .update("completedTasks", FieldValue.increment(1));
            refreshTasks();
            loadStats();
        });

        row.add(lbl, BorderLayout.CENTER);
        row.add(done, BorderLayout.EAST);
        return row;
    }

    /* ───────────── STATS + REFRESH ───────────── */

    private void loadStats() {
        new Thread(() -> {
            try {
                DocumentSnapshot doc =
                        db.collection("users").document(uid).get().get();

                long total = doc.getLong("totalTasks") != null
                        ? doc.getLong("totalTasks") : 0;

                long completed = doc.getLong("completedTasks") != null
                        ? doc.getLong("completedTasks") : 0;

                SwingUtilities.invokeLater(() ->
                        statsLabel.setText(
                                "Total: " + total + " | Done: " + completed
                        )
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(5000, e -> refreshTasks());
        refreshTimer.start();
    }

    private void refreshTasks() {
        SwingUtilities.invokeLater(() -> {
            listPanel.removeAll();
            loadTasks();
        });
    }

    private void startClock(JLabel label) {
        Timer clock = new Timer(1000, e ->
                label.setText(java.time.LocalTime.now().withSecond(0).toString())
        );
        clock.start();
    }

    @Override
    public void dispose() {
        if (refreshTimer != null) refreshTimer.stop();
        super.dispose();
    }
}
