package com.uvarubber.dao;

import com.uvarubber.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CollectionDAO {

    // This method saves a daily collection record into the MySQL table
    public void saveCollection(int supplierId, String date, double liters, int metrolac, double drc, double dryKg) {
        String sql = "INSERT INTO daily_collections (supplier_id, collection_date, liters, metrolac_reading, drc_percentage, dry_kg) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
            pstmt.setString(2, date); // Format: YYYY-MM-DD
            pstmt.setDouble(3, liters);
            pstmt.setInt(4, metrolac);
            pstmt.setDouble(5, drc);
            pstmt.setDouble(6, dryKg);

            pstmt.executeUpdate();
            System.out.println("Data saved successfully to MySQL!");

        } catch (SQLException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}