package com.voxcom.vox;

import com.google.cloud.firestore.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final Firestore db;
    private final JPanel listPanel;
    private final String uid;
    private final String email;
    private final String username;
    private Timer refreshTimer;


    public DashboardFrame(String uid, String email, String username) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.db = FirebaseService.getDB();

        setTitle("VOX – " + username);
        setSize(520, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(listPanel);
        add(scroll);

        loadTasks();
        startAutoRefresh();
    }
    private void startAutoRefresh() {
        // refresh every 5 seconds
        refreshTimer = new Timer(5000, e -> {
            refreshTasks();
        });
        refreshTimer.start();
    }
    private void refreshTasks() {
        SwingUtilities.invokeLater(() -> {
            listPanel.removeAll(); 
            loadTasks();           
        });
    }


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

                if (task == null || task.title == null) {
                    System.out.println("Skipping invalid task: " + doc.getId());
                    continue;
                }

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
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.BLACK);
        row.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

        // Fixed height
        row.setPreferredSize(new Dimension(480, 60));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        String expiry = task.expiresAt.toDate().toString();

        JLabel lbl = new JLabel(
                "<html><b>" + task.title + "</b><br><small>" + expiry + "</small></html>"
        );
        lbl.setForeground(Color.YELLOW);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton done = new JButton("✔");
        done.addActionListener(e ->
                tasks.document(taskId).update("completed", true)
        );

        row.add(lbl, BorderLayout.CENTER);
        row.add(done, BorderLayout.EAST);
        return row;
    }
}
