package com.uvarubber.view;

import com.uvarubber.dao.CollectionDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class PaymentFrame extends JFrame {
    private JTextField txtStartDate, txtEndDate, txtGlobalRate;
    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private CollectionDAO collectionDAO = new CollectionDAO();
    private final Color ENV_GREEN = new Color(34, 139, 34);

    public PaymentFrame() {
        setTitle("Uva Rubber - Payment Summary");
        setSize(1000, 600);
        setLayout(new BorderLayout(10, 10));

        // --- TOP: Filters ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Cycle Details"));

        topPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        txtStartDate = new JTextField(10);
        topPanel.add(txtStartDate);

        topPanel.add(new JLabel("End Date:"));
        txtEndDate = new JTextField(10);
        topPanel.add(txtEndDate);

        topPanel.add(new JLabel("Standard Rate:"));
        txtGlobalRate = new JTextField("575", 5); // Default rate
        topPanel.add(txtGlobalRate);

        JButton btnCalculate = new JButton("Generate Summary");
        btnCalculate.setBackground(ENV_GREEN);
        btnCalculate.setForeground(Color.WHITE);
        btnCalculate.addActionListener(e -> generateReport());
        topPanel.add(btnCalculate);

        add(topPanel, BorderLayout.NORTH);

        // --- CENTER: Summary Table ---
        String[] columns = {"Farmer Name", "Bank", "Account No", "Total Dry KG", "Rate (Editable)", "Total Payout"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only the RATE column is editable for special cases
            }
        };
        paymentTable = new JTable(tableModel);
        paymentTable.setRowHeight(25);

        // Listener to recalculate total if manager changes a specific rate
        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == 4) { // If Rate is changed
                int row = e.getFirstRow();
                recalculateRow(row);
            }
        });

        // --- RIGHT-CLICK MENU FOR PRINTING ---
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem printItem = new JMenuItem("Print Payment Slip (PDF/Printer)");

        printItem.addActionListener(e -> {
            int row = paymentTable.getSelectedRow();
            if (row != -1) {
                // Get data from the selected row
                String name = paymentTable.getValueAt(row, 0).toString();
                String endDate = txtEndDate.getText();
                double rate = Double.parseDouble(paymentTable.getValueAt(row, 4).toString());

                // Fetch daily records for this specific farmer to show on the receipt
                // For your BIT project, you'll need a method in DAO to get collections by Name/Date
                // For now, let's assume 'allCollections' is passed or fetched
                List<Object[]> collections = collectionDAO.getAllCollections(); // Filter this by name in a real scenario

                printReceipt(name, endDate, collections, rate);
            }
        });
        popupMenu.add(printItem);

        // Attach the menu to the table
        paymentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = paymentTable.rowAtPoint(e.getPoint());
                    paymentTable.setRowSelectionInterval(row, row);
                    popupMenu.show(paymentTable, e.getX(), e.getY());
                }
            }
        });

        add(new JScrollPane(paymentTable), BorderLayout.CENTER);

        // --- BOTTOM: Actions ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnPrint = new JButton("PRINT RECEIPT");
        styleButton(btnPrint, new Color(255, 140, 0), new Dimension(200, 45)); // Orange color
        btnPrint.addActionListener(e -> {
            int row = paymentTable.getSelectedRow();
            if (row != -1) {
                String name = tableModel.getValueAt(row, 0).toString();
                String start = txtStartDate.getText();
                String end = txtEndDate.getText();
                double baseRate = Double.parseDouble(tableModel.getValueAt(row, 4).toString());

                // 1. Fetch details from DAO
                List<Object[]> collections = collectionDAO.getCollectionsBySupplier(name, start, end);

                // 2. Send to Printer
                printReceipt(name, end, collections, baseRate);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a farmer from the table first!");
            }
        });
        bottomPanel.add(btnPrint);

        JButton btnExport = new JButton("Export to Bank List");
        btnExport.addActionListener(e -> JOptionPane.showMessageDialog(this, "Exporting People's Bank Credit List..."));
        bottomPanel.add(btnExport);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void generateReport() {
        tableModel.setRowCount(0);
        String start = txtStartDate.getText();
        String end = txtEndDate.getText();
        double globalRate = Double.parseDouble(txtGlobalRate.getText());

        List<Object[]> data = collectionDAO.getPaymentSummary(start, end);
        for (Object[] row : data) {
            double kg = (double) row[3];
            double rate = Double.parseDouble(txtGlobalRate.getText());
            double total = kg * rate;

            tableModel.addRow(new Object[]{
                    row[0],
                    row[1],
                    row[2],
                    String.format("%.2f", kg), // Forces 2 decimal places for KG
                    rate,
                    String.format("%.2f", total) // Forces 2 decimal places for LKR
            });
        }
    }

    private void recalculateRow(int row) {
        try {
            double kg = (double) tableModel.getValueAt(row, 3);
            double newRate = Double.parseDouble(tableModel.getValueAt(row, 4).toString());
            double newTotal = kg * newRate;
            tableModel.setValueAt(String.format("%.2f", newTotal), row, 5);
        } catch (Exception e) {
            // If user enters invalid rate
        }
    }

    public void printReceipt(String name, String date, List<Object[]> collections, double baseRate) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));

            int y = 20;
            int x = 50;

            // Header
            g2d.drawString("🌿 UVA RUBBER COLLECTION CENTER", x, y); y += 15;
            g2d.drawString("---------------------------------", x, y); y += 15;
            g2d.drawString("Supplier: " + name, x, y); y += 15;
            g2d.drawString("Date: " + date, x, y); y += 20;

            // Table Header
            g2d.drawString(String.format("%-12s %-8s %-8s %-8s", "Date", "Liters", "DRC%", "DryKG"), x, y); y += 10;
            g2d.drawString("---------------------------------", x, y); y += 15;

            double totalDryKg = 0;
            for (Object[] row : collections) {
                String cDate = row[1].toString();
                double liters = (double) row[2];
                double drc = (double) row[3];
                double dryKg = (double) row[4];
                totalDryKg += dryKg;

                g2d.drawString(String.format("%-12s %-8.1f %-8.1f %-8.2f", cDate, liters, drc, dryKg), x, y);
                y += 15;
            }

            // Calculations (Your Stamp Duty Logic)
            double billRate = baseRate + 4;
            double grossAmount = totalDryKg * billRate;
            double stampDuty = totalDryKg * 4;
            double netTotal = grossAmount - stampDuty;

            y += 15;
            g2d.drawString("---------------------------------", x, y); y += 15;
            g2d.drawString("Total Dry KG: " + String.format("%.2f", totalDryKg), x, y); y += 15;
            g2d.drawString("Bill Rate (+4): " + billRate, x, y); y += 15;
            g2d.drawString("Gross Amount: " + String.format("%.2f", grossAmount), x, y); y += 15;
            g2d.drawString("Stamp Duty:  -" + String.format("%.2f", stampDuty), x, y); y += 15;
            g2d.setFont(new Font("Monospaced", Font.BOLD, 11));
            g2d.drawString("NET PAYABLE:  LKR " + String.format("%.2f", netTotal), x, y); y += 30;

            g2d.setFont(new Font("Monospaced", Font.ITALIC, 9));
            g2d.drawString("Authorized By: _________________", x, y);

            return Printable.PAGE_EXISTS;
        });

        if (job.printDialog()) {
            try { job.print(); } catch (PrinterException e) { e.printStackTrace(); }
        }
    }
    private void styleButton(JButton btn, Color bg, Dimension size) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(size);
        btn.setCursor(new Cursor(java.awt.Cursor.HAND_CURSOR));
    }
    private String formatCurrency(double amount) {
        // This creates a format with a space as a grouping separator
        java.text.DecimalFormatSymbols symbols = new java.text.DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00", symbols);
        return df.format(amount);
    }
}