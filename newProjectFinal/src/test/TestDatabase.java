package test;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TestDatabase {
    public static void main(String[] args) {
        // Test the database connection
        testConnection();

        // Test adding a record to the database
        testAddRecord();
    }

    /**
     * Tests the database connection by attempting to connect.
     */
    private static void testConnection() {
        Connection conn = null;
        try {
            // Obtain a new database connection
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure the connection is closed
            DatabaseConnection.closeConnection(conn);
        }
    }

    /**
     * Tests inserting a record into the HealthRecords table.
     */
    private static void testAddRecord() {
        String sql = "INSERT INTO HealthRecords (patient_id, metric, value) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Obtain a new database connection
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                // Prepare the SQL query with placeholders
                stmt = conn.prepareStatement(sql);

                // Set the parameters for the query
                stmt.setString(1, "456"); // patient_id
                stmt.setString(2, "Blood Pressure"); // metric
                stmt.setString(3, "120/80"); // value

                // Execute the SQL query
                int rowsAffected = stmt.executeUpdate();
                System.out.println(rowsAffected + " record(s) added successfully!");
            } else {
                System.out.println("Failed to obtain a database connection.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the statement and connection
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseConnection.closeConnection(conn);
        }
    }
}




