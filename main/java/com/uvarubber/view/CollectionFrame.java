package com.uvarubber.view;

import com.uvarubber.dao.CollectionDAO;
import com.uvarubber.dao.SupplierDAO;
import com.uvarubber.model.Supplier;
import com.uvarubber.service.RubberService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.List;

public class CollectionFrame extends JFrame {
    // Styling Constants
    private final Color ENV_GREEN = new Color(34, 139, 34);
    private final Color DELETE_RED = new Color(178, 34, 34);
    private final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 22);

    // Components
    private JComboBox<Object> supplierCombo;
    private JTextField txtLiters, txtMetrolac;
    private JButton btnSave, btnDelete;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    // Backend Tools
    private RubberService rubberService = new RubberService();
    private SupplierDAO supplierDAO = new SupplierDAO();
    private CollectionDAO collectionDAO = new CollectionDAO();
    private List<Supplier> allSuppliers;

    public CollectionFrame() {
        setTitle("Uva Rubber System");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // --- 1. HEADER SECTION ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel lblLogo = new JLabel("🌿 UVA RUBBER ");
        lblLogo.setFont(HEADER_FONT);
        lblLogo.setForeground(ENV_GREEN);
        headerPanel.add(lblLogo);

        JLabel lblTitle = new JLabel("| Daily Collection Entry");
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. MAIN CONTENT PANEL (Input + Buttons) ---
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setBackground(Color.WHITE);

        // 2a. Input Grid
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        inputPanel.add(createStyledLabel("Select Farmer:"));
        supplierCombo = new JComboBox<>();
        supplierCombo.setEditable(true);
        supplierCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loadSuppliers();
        setupSearchableCombo();
        inputPanel.add(supplierCombo);

        inputPanel.add(createStyledLabel("Liters (L):"));
        txtLiters = new JTextField();
        txtLiters.setFont(BOLD_FONT);
        txtLiters.addActionListener(e -> txtMetrolac.requestFocus());
        inputPanel.add(txtLiters);

        inputPanel.add(createStyledLabel("Metrolac Reading:"));
        txtMetrolac = new JTextField();
        txtMetrolac.setFont(BOLD_FONT);
        txtMetrolac.addActionListener(e -> saveData());
        inputPanel.add(txtMetrolac);

        // 2b. Button Panel (Located at the bottom of input section)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnSave = new JButton("SAVE RECORD");
        styleButton(btnSave, ENV_GREEN, new Dimension(200, 45));
        btnSave.addActionListener(e -> saveData());
        buttonPanel.add(btnSave);

        // New: Add Farmer Button
        JButton btnAddFarmer = new JButton("ADD NEW FARMER");
        styleButton(btnAddFarmer, new Color(70, 130, 180), new Dimension(200, 45)); // Steel Blue color
        btnAddFarmer.addActionListener(e -> {
            new SupplierFrame().setVisible(true);
            // Optional: You might want to reload the combo box when the window closes
        });
        buttonPanel.add(btnAddFarmer);

        btnDelete = new JButton("DELETE SELECTED");
        styleButton(btnDelete, DELETE_RED, new Dimension(200, 45));
        btnDelete.addActionListener(e -> deleteSelectedRecord());
        buttonPanel.add(btnDelete);

        centerContainer.add(inputPanel);
        centerContainer.add(buttonPanel);
        add(centerContainer, BorderLayout.CENTER);

        // --- 3. HISTORY TABLE SECTION ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), " Recent Collections History ", 0, 0, BOLD_FONT, ENV_GREEN));

        String[] columns = {"ID", "Date", "Farmer", "Liters", "Metrolac", "Dry KG"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        historyTable = new JTable(tableModel);
        historyTable.setRowHeight(30);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablePanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        tablePanel.setPreferredSize(new Dimension(800, 300));

        add(tablePanel, BorderLayout.SOUTH);

        refreshTable();
        clearForm();
        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BOLD_FONT);
        return label;
    }

    private void styleButton(JButton btn, Color bg, Dimension size) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(size);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void loadSuppliers() {
        allSuppliers = supplierDAO.getAllSuppliers();
        for (Supplier s : allSuppliers) supplierCombo.addItem(s);
    }

    private void setupSearchableCombo() {
        JTextField editor = (JTextField) supplierCombo.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER) return;
                String input = editor.getText();
                supplierCombo.hidePopup();
                supplierCombo.removeAllItems();
                for (Supplier s : allSuppliers) {
                    if (s.getName().toLowerCase().contains(input.toLowerCase())) supplierCombo.addItem(s);
                }
                editor.setText(input);
                if (supplierCombo.getItemCount() > 0) supplierCombo.showPopup();
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Object[]> rows = collectionDAO.getAllCollections();
        for (Object[] row : rows) tableModel.addRow(row);
        historyTable.getColumnModel().getColumn(0).setMinWidth(0);
        historyTable.getColumnModel().getColumn(0).setMaxWidth(0);
    }

    private void saveData() {
        try {
            Object selected = supplierCombo.getSelectedItem();
            if (!(selected instanceof Supplier)) {
                JOptionPane.showMessageDialog(this, "Select a valid farmer.");
                return;
            }
            Supplier s = (Supplier) selected;
            double l = Double.parseDouble(txtLiters.getText());
            int m = Integer.parseInt(txtMetrolac.getText());
            double d = rubberService.calculateDRC(m);
            double w = rubberService.calculateDryWeight(l, m);

            collectionDAO.saveCollection(s.getId(), LocalDate.now().toString(), l, m, d, w);
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Success!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Check inputs!");
        }
    }

    private void deleteSelectedRecord() {
        int selectedRow = historyTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 2);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete record for " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            collectionDAO.deleteCollection(id);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Deleted.");
        }
    }

    private void clearForm() {
        txtLiters.setText("");
        txtMetrolac.setText("");
        supplierCombo.setSelectedIndex(-1);
        supplierCombo.getEditor().setItem("");
        supplierCombo.requestFocus();
    }
}