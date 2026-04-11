package com.uvarubber.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/uva_rubber_system";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASSWORD = "kings123"; // Your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found!");
        }
    }
}