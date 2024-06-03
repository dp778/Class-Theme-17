package com.company;

import java.sql.*;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost/vis3_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
        return conn;
    }
}