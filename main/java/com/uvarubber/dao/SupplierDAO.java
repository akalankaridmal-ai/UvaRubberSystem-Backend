package com.uvarubber.dao;

import com.uvarubber.model.Supplier;
import com.uvarubber.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    public boolean addSupplier(String name, String bank, String branch, String acc, String nic) {
        String sql = "INSERT INTO suppliers (supplier_name, bank_name, branch_name, account_no, nic_no) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = com.uvarubber.util.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, bank);
            pstmt.setString(3, branch);
            pstmt.setString(4, acc);
            pstmt.setString(5, nic);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get all suppliers from the database
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("bank_name"),
                        rs.getString("branch_name"),
                        rs.getString("account_no"),
                        rs.getString("nic_no")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }
}