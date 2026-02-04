package com.voxcom.vox;

import com.google.cloud.firestore.*;
import com.voxcom.vox.ui.*;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final Firestore db;
    private JLabel loaderLabel;
    private Timer loaderTimer;
    private int loaderIndex = 0;

    public LoginFrame() {
        db = FirebaseService.getDB();

        setTitle("VOX CONNECT");
        setSize(380, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(VoxTheme.BG);

        /* ───────── TITLE ───────── */

        JLabel title = new JLabel("VOX CONNECT");
        title.setFont(FontUtil.PIXEL_16);
        title.setForeground(VoxTheme.YELLOW);
        title.setBounds(110, 20, 200, 30);
        add(title);

        /* ───────── INPUTS ───────── */

        PixelTextField emailField = new PixelTextField();
        emailField.setBounds(50, 80, 280, 40);
        add(emailField);

        PixelTextField codeField = new PixelTextField();
        codeField.setBounds(50, 130, 280, 40);
        add(codeField);

        /* ───────── BUTTON ───────── */

        PixelButton connect = new PixelButton("CONNECT");
        connect.setBounds(130, 185, 120, 40);
        add(connect);

        /* ───────── LOADER ───────── */

        loaderLabel = new JLabel();
        loaderLabel.setBounds(174, 235, 32, 32);
        loaderLabel.setVisible(false);
        add(loaderLabel);

        /* ───────── ACTION ───────── */

        connect.addActionListener(e -> {
            connect.setEnabled(false);
            startLoader();

            new Thread(() -> {
                login(emailField, codeField);

                SwingUtilities.invokeLater(() -> {
                    stopLoader();
                    connect.setEnabled(true);
                });
            }).start();
        });

        setIconImage(
            new ImageIcon(
                getClass().getResource("/icons/voxicon.png")
            ).getImage()
        );
    }

    /* ───────── LOADER ANIMATION ───────── */

    private void startLoader() {
        loaderIndex = 0;
        loaderLabel.setVisible(true);

        ImageIcon icon1 = new ImageIcon(
                getClass().getResource("/loader/loading1.png")
        );
        ImageIcon icon2 = new ImageIcon(
                getClass().getResource("/loader/loading2.png")
        );

        loaderLabel.setIcon(icon1); // first frame

        loaderTimer = new Timer(350, e -> {
            loaderLabel.setIcon(
                    loaderIndex % 2 == 0 ? icon1 : icon2
            );
            loaderIndex++;
        });

        loaderTimer.start();
        loaderLabel.repaint();
    }

    private void stopLoader() {
        if (loaderTimer != null) loaderTimer.stop();
        loaderLabel.setVisible(false);
    }

    /* ───────── LOGIN LOGIC ───────── */

    private void login(JTextField emailField, JTextField codeField) {
        try {
            String email = emailField.getText().trim();
            String code = codeField.getText().trim();

            QuerySnapshot users =
                    db.collection("users")
                      .whereEqualTo("email", email)
                      .whereEqualTo("accessCode", code)
                      .get()
                      .get();

            if (users.isEmpty()) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(
                                this,
                                "INVALID EMAIL OR ACCESS CODE",
                                "ACCESS DENIED",
                                JOptionPane.ERROR_MESSAGE
                        )
                );
                return;
            }

            DocumentSnapshot user = users.getDocuments().get(0);
            String uid = user.getId();
            String username = user.getString("username");

            SessionManager.saveSession(uid, email, username);

            SwingUtilities.invokeLater(() -> {
                dispose();
                new DashboardFrame(uid, email, username).setVisible(true);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
