package com.uvarubber.view;

import com.uvarubber.dao.SupplierDAO;
import com.uvarubber.model.Supplier;

import javax.swing.*;
import java.awt.*;

public class SupplierFrame extends JFrame {
    private JTextField txtName, txtNIC, txtBank, txtBranch, txtAccount;
    private final Color ENV_GREEN = new Color(34, 139, 34);
    private final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private SupplierDAO supplierDAO = new SupplierDAO();

    public SupplierFrame() {
        setTitle("Uva Rubber - Farmer Registration");
        setSize(500, 500);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(Color.WHITE);

        // Header
        JLabel lblHeader = new JLabel("Register New Farmer", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeader.setForeground(ENV_GREEN);
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblHeader, BorderLayout.NORTH);

        // Input Form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 20));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 40));

        formPanel.add(createLabel("Full Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(createLabel("NIC Number:"));
        txtNIC = new JTextField();
        formPanel.add(txtNIC);

        formPanel.add(createLabel("Bank Name (e.g. BOC):"));
        txtBank = new JTextField();
        formPanel.add(txtBank);

        formPanel.add(createLabel("Branch:"));
        txtBranch = new JTextField();
        formPanel.add(txtBranch);

        formPanel.add(createLabel("Account Number:"));
        txtAccount = new JTextField();
        formPanel.add(txtAccount);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JButton btnRegister = new JButton("REGISTER FARMER");
        btnRegister.setBackground(ENV_GREEN);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(BOLD_FONT);
        btnRegister.setFocusPainted(false);
        btnRegister.setPreferredSize(new Dimension(0, 50));

        btnRegister.addActionListener(e -> registerSupplier());
        add(btnRegister, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center on screen
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BOLD_FONT);
        return label;
    }

    private void registerSupplier() {
        String name = txtName.getText();
        String nic = txtNIC.getText();
        String bank = txtBank.getText();
        String branch = txtBranch.getText();
        String account = txtAccount.getText();

        if (name.isEmpty() || account.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Account Number are required!");
            return;
        }

        // Call DAO to save (We will update the DAO method next)
        boolean success = supplierDAO.addSupplier(name, bank, branch, account, nic);

        if (success) {
            JOptionPane.showMessageDialog(this, "Farmer Registered Successfully!");
            dispose(); // Close this window
        } else {
            JOptionPane.showMessageDialog(this, "Error saving to database.");
        }
    }
}
