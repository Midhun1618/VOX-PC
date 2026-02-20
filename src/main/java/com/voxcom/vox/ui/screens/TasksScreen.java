package com.voxcom.vox.ui.screens;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.QuerySnapshot;
import com.voxcom.vox.FirebaseService;
import com.voxcom.vox.ui.theme.PixelButton;
import com.voxcom.vox.ui.theme.PixelTextField;
import com.voxcom.vox.ui.theme.VoxTheme;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TasksScreen extends JPanel {

    private final JPanel listPanel;
    private final String uid;
    private final PixelTextField inputField;

    public TasksScreen(String uid) {

        this.uid = uid;
        setLayout(new BorderLayout());
        setBackground(VoxTheme.BG);

        // TOP BAR
        JPanel top = new JPanel(new BorderLayout(8,0));
        top.setBackground(VoxTheme.BG);
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        inputField = new PixelTextField();
        PixelButton addBtn = new PixelButton("ADD");

        addBtn.addActionListener(e -> createTask());

        top.add(inputField, BorderLayout.CENTER);
        top.add(addBtn, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // LIST
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(VoxTheme.BG);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(VoxTheme.BG);

        add(scroll, BorderLayout.CENTER);

        loadTasks();
    }

    private void createTask() {

        String text = inputField.getText().trim();
        if(text.isEmpty()) return;

        inputField.setText("");

        new Thread(() -> {
            try {

                Date now = new Date();
                Date expiry = new Date(now.getTime() + 24*60*60*1000); // +24h

                Map<String, Object> task = new HashMap<>();
                task.put("title", text);
                task.put("completed", false);
                task.put("createdAt", Timestamp.of(now));
                task.put("expiresAt", Timestamp.of(expiry));

                FirebaseService.getDB()
                        .collection("users")
                        .document(uid)
                        .collection("tasks")
                        .add(task)
                        .get();

                SwingUtilities.invokeLater(this::loadTasks);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void loadTasks() {

        listPanel.removeAll();
        listPanel.add(label("Loading tasks..."));

        new Thread(() -> {
            try {

                QuerySnapshot snap = FirebaseService.getDB()
                        .collection("users")
                        .document(uid)
                        .collection("tasks")
                        .get()
                        .get();

                Date now = new Date();

                SwingUtilities.invokeLater(() -> {

                    listPanel.removeAll();

                   snap.getDocuments().forEach(doc -> {

                        String title = doc.getString("title");
                        Boolean completed = doc.getBoolean("completed");
                        Timestamp expiresAt = doc.getTimestamp("expiresAt");

                        boolean active = Boolean.FALSE.equals(completed)
                                && expiresAt != null
                                && expiresAt.toDate().after(now);

                        if(active)
                            listPanel.add(taskCard(doc.getId(), title));
                    });

                    if(listPanel.getComponentCount()==0)
                        listPanel.add(label("No active tasks"));

                    listPanel.revalidate();
                    listPanel.repaint();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private JLabel taskLabel(String t) {
        JLabel lbl = new JLabel(t);
        lbl.setForeground(VoxTheme.YELLOW);
        lbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return lbl;
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.GRAY);
        l.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return l;
    }
    private JPanel taskCard(String docId, String title) {

        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        card.setBackground(new Color(25,25,25));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70,70,70)),
                BorderFactory.createEmptyBorder(8,10,8,10)
        ));

        JLabel lbl = new JLabel(title);
        lbl.setForeground(VoxTheme.YELLOW);

        JButton doneBtn = new JButton("DONE");
        doneBtn.setFocusPainted(false);
        doneBtn.setBackground(new Color(60,140,90));
        doneBtn.setForeground(Color.WHITE);

        doneBtn.addActionListener(e -> markDone(docId));

        card.add(lbl, BorderLayout.CENTER);
        card.add(doneBtn, BorderLayout.EAST);

        return card;
    }
    private void markDone(String docId) {

        new Thread(() -> {
            try {

                FirebaseService.getDB()
                        .collection("users")
                        .document(uid)
                        .collection("tasks")
                        .document(docId)
                        .update("completed", true)
                        .get();

                SwingUtilities.invokeLater(this::loadTasks);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


}
