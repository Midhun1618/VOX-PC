package com.voxcom.vox.ui.frame;

import com.google.cloud.firestore.QuerySnapshot;
import com.voxcom.vox.FirebaseService;
import com.voxcom.vox.SessionManager;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField = new JTextField();
    private JTextField codeField = new JTextField();
    private JLabel status = new JLabel(" ");

    public LoginFrame() {

        setTitle("VOX Login");
        setSize(350, 260);
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/voxicon.png"));
        setIconImage(icon);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5,1,5,5));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        panel.add(new JLabel("Email"));
        panel.add(emailField);

        panel.add(new JLabel("Code"));
        panel.add(codeField);

        JButton loginBtn = new JButton("Connect");
        panel.add(loginBtn);

        add(panel, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> attemptLogin());
    }

    private void attemptLogin() {

        String email = emailField.getText().trim();
        String code  = codeField.getText().trim();

        status.setText("Connecting...");

        new Thread(() -> {

            try {

                QuerySnapshot query = FirebaseService.getDB()
                        .collection("users")
                        .whereEqualTo("email", email)
                        .whereEqualTo("accessCode", code)
                        .get()
                        .get();

                if(query.isEmpty()) {
                    SwingUtilities.invokeLater(() ->
                            status.setText("Invalid email or code"));
                    return;
                }

                var doc = query.getDocuments().get(0);

                String uid = doc.getId();
                String username = doc.getString("username");

                Long avatarLong = doc.getLong("avatarIndex");
                int avatarIndex = avatarLong != null ? avatarLong.intValue() : 1;
                System.out.println("Avatar index from DB = " + avatarIndex);


                SessionManager.save(uid, email, username, avatarIndex);

                SwingUtilities.invokeLater(() -> {
                    dispose();
                    new DashboardFrame(uid, email, username, avatarIndex).setVisible(true);
                });

            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() ->
                        status.setText("Connection failed"));
            }

        }).start();
    }
}
