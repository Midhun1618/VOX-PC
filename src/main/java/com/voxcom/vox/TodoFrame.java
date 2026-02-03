package com.voxcom.vox;


import com.google.cloud.firestore.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TodoFrame extends JFrame {

    private Firestore db;
    private JPanel listPanel;

    public TodoFrame(String email, String username) {
        db = FirebaseService.getDB();

        setTitle("VOX - " + username);
        setSize(520, 600);
        setLocationRelativeTo(null);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(listPanel);
        add(scroll);

        loadTasksByEmail(email);
    }

    private void loadTasksByEmail(String email) {
        try {
            QuerySnapshot users =
                    db.collection("users")
                      .whereEqualTo("email", email)
                      .get()
                      .get();

            if (users.isEmpty()) return;

            String uid = users.getDocuments().get(0).getId();

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

                String title = doc.getString("title");
                String expiry =
                        doc.getTimestamp("expiresAt")
                           .toDate()
                           .toString();

                JPanel row = createTaskRow(tasks, doc.getId(), title, expiry);
                listPanel.add(row);
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
            String title,
            String expiry
    ) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.BLACK);
        row.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

        JLabel lbl = new JLabel(title + " | " + expiry);
        lbl.setForeground(Color.YELLOW);

        JButton update = new JButton("✔");
        update.addActionListener(e ->
                tasks.document(taskId)
                     .update("completed", true)
        );

        row.add(lbl, BorderLayout.CENTER);
        row.add(update, BorderLayout.EAST);
        return row;
    }
}
