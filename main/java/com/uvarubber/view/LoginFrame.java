package com.uvarubber.view;

import com.uvarubber.dao.UserDAO;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private UserDAO userDAO = new UserDAO();
    private final Color ENV_GREEN = new Color(34, 139, 34);

    public LoginFrame() {
        setTitle("Uva Rubber - Security Portal");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(ENV_GREEN);
        JLabel lblTitle = new JLabel("SYSTEM LOGIN");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(lblTitle);
        add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 20));
        form.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        form.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        form.add(txtUsername);

        form.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        form.add(txtPassword);
        add(form, BorderLayout.CENTER);

        // Login Button
        JButton btnLogin = new JButton("ACCESS SYSTEM");
        btnLogin.setBackground(ENV_GREEN);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(e -> handleLogin());
        add(btnLogin, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (userDAO.authenticate(user, pass)) {
            new CollectionFrame(); // Open the main dashboard
            this.dispose(); // Close login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}