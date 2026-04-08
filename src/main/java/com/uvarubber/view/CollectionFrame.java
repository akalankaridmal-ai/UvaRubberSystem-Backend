package com.uvarubber.view;

import com.uvarubber.dao.CollectionDAO;
import com.uvarubber.dao.SupplierDAO;
import com.uvarubber.model.Supplier;
import com.uvarubber.service.RubberService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class CollectionFrame extends JFrame {
    private JComboBox<Supplier> supplierCombo;
    private JTextField txtLiters, txtMetrolac;
    private JLabel lblDRC, lblDryKg;
    private JButton btnSave;

    private RubberService rubberService = new RubberService();
    private SupplierDAO supplierDAO = new SupplierDAO();
    private CollectionDAO collectionDAO = new CollectionDAO();

    public CollectionFrame() {
        setTitle("Uva Rubber - Daily Collection");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        // 1. Supplier Selection
        add(new JLabel(" Select Farmer:"));
        supplierCombo = new JComboBox<>();
        loadSuppliers();
        add(supplierCombo);

        // 2. Input Fields
        add(new JLabel(" Liters:"));
        txtLiters = new JTextField();
        add(txtLiters);

        add(new JLabel(" Metrolac Reading:"));
        txtMetrolac = new JTextField();
        add(txtMetrolac);

        // 3. Calculation Display
        add(new JLabel(" Calculated DRC (%):"));
        lblDRC = new JLabel("0.0");
        add(lblDRC);

        add(new JLabel(" Total Dry KG:"));
        lblDryKg = new JLabel("0.0");
        add(lblDryKg);

        // 4. Action Button
        btnSave = new JButton("Save Collection");
        add(btnSave);

        // Logic for Calculation when user types
        btnSave.addActionListener(e -> saveData());

        setVisible(true);
    }

    private void loadSuppliers() {
        List<Supplier> list = supplierDAO.getAllSuppliers();
        for (Supplier s : list) {
            supplierCombo.addItem(s);
        }
    }

    private void saveData() {
        try {
            Supplier selected = (Supplier) supplierCombo.getSelectedItem();
            double liters = Double.parseDouble(txtLiters.getText());
            int metrolac = Integer.parseInt(txtMetrolac.getText());

            double drc = rubberService.calculateDRC(metrolac);
            double dryKg = rubberService.calculateDryWeight(liters, metrolac);

            lblDRC.setText(String.valueOf(drc));
            lblDryKg.setText(String.valueOf(dryKg));

            collectionDAO.saveCollection(selected.getId(), LocalDate.now().toString(), liters, metrolac, drc, dryKg);
            JOptionPane.showMessageDialog(this, "Successfully Saved!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Please enter valid numbers");
        }
    }
}