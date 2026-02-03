package com.voxcom.vox;

import com.google.cloud.firestore.*;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final Firestore db;

    public LoginFrame() {
        db = FirebaseService.getDB();

        setTitle("VOX PC Login");
        setSize(360, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.BLACK);

        JLabel title = new JLabel("VOX CONNECT");
        title.setForeground(Color.YELLOW);
        title.setBounds(120, 20, 150, 20);

        JTextField emailField = new JTextField();
        emailField.setBounds(50, 70, 260, 28);

        JPasswordField codeField = new JPasswordField();
        codeField.setBounds(50, 110, 260, 28);

        JButton connect = new JButton("CONNECT");
        connect.setBounds(120, 170, 120, 30);

        connect.addActionListener(e -> login(emailField, codeField));

        panel.add(title);
        panel.add(emailField);
        panel.add(codeField);
        panel.add(connect);

        add(panel);
    }

    private void login(JTextField emailField, JPasswordField codeField) {
        try {
            String email = emailField.getText().trim();
            String code = new String(codeField.getPassword()).trim();

            QuerySnapshot users =
                    db.collection("users")
                      .whereEqualTo("email", email)
                      .whereEqualTo("accessCode", code)
                      .get()
                      .get();

            if (users.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid email or access code");
                return;
            }

            DocumentSnapshot user = users.getDocuments().get(0);
            String uid = user.getId();
            String username = user.getString("username");

            dispose();
            new DashboardFrame(uid, email, username).setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
