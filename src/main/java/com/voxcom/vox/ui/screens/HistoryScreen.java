package com.voxcom.vox.ui.screens;

import com.google.cloud.firestore.*;
import com.voxcom.vox.FirebaseService;
import com.google.cloud.Timestamp;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class HistoryScreen extends JPanel {

    private final String uid;
    private final JPanel listPanel;

    public HistoryScreen(String uid) {

        this.uid = uid;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.BLACK);

        add(scroll, BorderLayout.CENTER);

        loadHistory();
    }

    private void loadHistory() {

        listPanel.add(label("Loading history..."));

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

                    for(DocumentSnapshot doc : snap.getDocuments()) {

                        String title = doc.getString("title");
                        Boolean completed = doc.getBoolean("completed");
                        Timestamp expiresAt = doc.getTimestamp("expiresAt");

                        boolean expired = expiresAt != null && expiresAt.toDate().before(now);
                        boolean done = Boolean.TRUE.equals(completed);

                        if(done || expired) {

                            if(done)
                                listPanel.add(historyLabel(title + " [ COMPLETED ] ", new Color(100,220,120)));
                            else
                                listPanel.add(historyLabel(title + " [ MISSED ]", new Color(220,80,80)));
                        }
                    }

                    if(listPanel.getComponentCount()==0)
                        listPanel.add(label("No history yet"));

                    listPanel.revalidate();
                    listPanel.repaint();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    private JLabel historyLabel(String t, Color c) {
        JLabel l = new JLabel(t);
        l.setForeground(c);
        l.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return l;
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.GRAY);
        l.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return l;
    }
}
