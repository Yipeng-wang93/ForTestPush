package server;

import database.DatabaseConnection;

import java.io.*; // Import necessary classes for input/output operations
import java.net.ServerSocket; // Import class for creating a server socket
import java.net.Socket; // Import class for client-server communication
import java.sql.Connection; // Import class for managing database connections
import java.sql.PreparedStatement; // Import class for executing SQL queries securely
import java.sql.ResultSet; // Import class for processing SQL query results
import java.sql.Timestamp; // Import class for handling SQL timestamp values

public class Server {
    public static void main(String[] args) {
        // Create a server socket on port 8080 and start listening for connections
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is running on port 8080...");
            while (true) {
                // Accept an incoming client connection
                Socket clientSocket = serverSocket.accept();
                // Handle the client connection in a new thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            // Print an error message if the server fails to start
            System.err.println("Error starting the server:");
            e.printStackTrace();
        }
    }

    /**
     * Handles client requests.
     * @param clientSocket The socket connected to the client.
     */
    private static void handleClient(Socket clientSocket) {
        // Use try-with-resources to ensure resources are closed after use
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Read the request sent by the client
            String request = in.readLine();
            if (request != null) {
                System.out.println("Received Request: " + request);

                // Process a GET request to fetch health records
                if (request.startsWith("GET:")) {
                    String[] parts = request.split(":");
                    String patientId = parts[1]; // Extract patient ID from the request
                    String metric = parts.length > 2 ? parts[2] : null; // Extract metric if provided
                    String response = fetchHealthRecords(patientId, metric); // Fetch records from the database
                    out.println(response); // Send the response to the client

                // Process an ADD request to add a new health record
                } else if (request.startsWith("ADD:")) {
                    String[] parts = request.split(":");
                    String patientId = parts[1]; // Extract patient ID
                    String metric = parts[2]; // Extract metric
                    String value = parts[3]; // Extract value
                    addHealthRecord(patientId, metric, value); // Add the record to the database
                    out.println("Record added successfully!"); // Confirm success to the client

                // Process an UPDATE_OR_INSERT request to update or add a record
                } else if (request.startsWith("UPDATE_OR_INSERT:")) {
                    String[] parts = request.split(":");
                    String patientId = parts[1]; // Extract patient ID
                    String metric = parts[2]; // Extract metric
                    String value = parts[3]; // Extract value
                    updateOrInsertHealthRecord(patientId, metric, value); // Update or add the record
                    out.println("Record updated or added successfully!"); // Confirm success to the client

                // Process a DELETE request to remove a health record
                } else if (request.startsWith("DELETE:")) {
                    String recordId = request.split(":")[1]; // Extract record ID
                    deleteHealthRecord(recordId); // Delete the record from the database
                    out.println("Record deleted successfully!"); // Confirm success to the client

                // Handle invalid request formats
                } else {
                    out.println("Invalid request format."); // Notify the client of invalid format
                }
            }
        } catch (IOException e) {
            // Handle errors during client communication
            System.err.println("Error handling client connection:");
            e.printStackTrace();
        }
    }

    /**
     * Fetch health records from the database for a given patient and metric.
     * @param patientId The ID of the patient.
     * @param metric The metric to filter by (optional).
     * @return A string representation of the fetched records.
     */
    private static String fetchHealthRecords(String patientId, String metric) {
        // SQL query to select health records based on patient ID and optionally a metric
        String sql = "SELECT RECORD_ID, PATIENT_ID, METRIC, VALUE, DATA_RECORDED FROM HEALTHRECORDS WHERE PATIENT_ID = ?";
        if (metric != null) {
            sql += " AND METRIC = ?"; // Add metric filter if provided
        }
        StringBuilder response = new StringBuilder(); // To hold the query result

        try (Connection conn = DatabaseConnection.getConnection(); // Establish database connection
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare SQL statement

            stmt.setString(1, patientId); // Set the patient ID parameter
            if (metric != null) {
                stmt.setString(2, metric); // Set the metric parameter if provided
            }

            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Append each record to the response string
                    response.append(rs.getString("RECORD_ID")).append(", ")
                            .append(rs.getString("PATIENT_ID")).append(", ")
                            .append(rs.getString("METRIC")).append(", ")
                            .append(rs.getString("VALUE")).append(", ")
                            .append(rs.getTimestamp("DATA_RECORDED") != null ? rs.getTimestamp("DATA_RECORDED").toString() : "N/A")
                            .append("\n");
                }
            }
        } catch (Exception e) {
            // Print an error message if the query fails
            System.err.println("Error fetching health records:");
            e.printStackTrace();
        }

        // Return the fetched records or a default message if no records are found
        return response.length() > 0 ? response.toString().trim() : "No records found.";
    }

    /**
     * Add a new health record to the database.
     * @param patientId The ID of the patient.
     * @param metric The metric to add.
     * @param value The value of the metric.
     */
    private static void addHealthRecord(String patientId, String metric, String value) {
        // SQL query to insert a new health record
        String sql = "INSERT INTO HEALTHRECORDS (PATIENT_ID, METRIC, VALUE) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); // Establish database connection
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare SQL statement

            stmt.setString(1, patientId); // Set the patient ID
            stmt.setString(2, metric); // Set the metric
            stmt.setString(3, value); // Set the value
            stmt.executeUpdate(); // Execute the insert query

        } catch (Exception e) {
            // Print an error message if the insertion fails
            System.err.println("Error adding health record:");
            e.printStackTrace();
        }
    }

    /**
     * Update an existing health record or insert a new one if it does not exist.
     * @param patientId The ID of the patient.
     * @param metric The metric to update or add.
     * @param value The value of the metric.
     */
    private static void updateOrInsertHealthRecord(String patientId, String metric, String value) {
        // SQL queries for checking, inserting, and updating records
        String checkSql = "SELECT RECORD_ID FROM HEALTHRECORDS WHERE PATIENT_ID = ? AND METRIC = ?";
        String insertSql = "INSERT INTO HEALTHRECORDS (PATIENT_ID, METRIC, VALUE) VALUES (?, ?, ?)";
        String updateSql = "UPDATE HEALTHRECORDS SET VALUE = ? WHERE RECORD_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection()) { // Establish database connection
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) { // Prepare check query
                checkStmt.setString(1, patientId); // Set patient ID
                checkStmt.setString(2, metric); // Set metric

                try (ResultSet rs = checkStmt.executeQuery()) { // Execute check query
                    if (rs.next()) {
                        // Update existing record if it exists
                        String recordId = rs.getString("RECORD_ID");
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, value); // Set the new value
                            updateStmt.setString(2, recordId); // Set the record ID
                            updateStmt.executeUpdate(); // Execute the update query
                            System.out.println("Updated record ID: " + recordId + " with new value: " + value);
                        }
                    } else {
                        // Insert a new record if no existing record is found
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setString(1, patientId); // Set patient ID
                            insertStmt.setString(2, metric); // Set metric
                            insertStmt.setString(3, value); // Set value
                            insertStmt.executeUpdate(); // Execute the insert query
                            System.out.println("Inserted new record for Patient ID: " + patientId + ", Metric: " + metric);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Print an error message if the operation fails
            System.err.println("Error updating or inserting health record:");
            e.printStackTrace();
        }
    }

    /**
     * Delete a health record from the database.
     * @param recordId The ID of the record to delete.
     */
    private static void deleteHealthRecord(String recordId) {
        // SQL query to delete a record based on record ID
        String sql = "DELETE FROM HEALTHRECORDS WHERE RECORD_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); // Establish database connection
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare delete query

            stmt.setString(1, recordId); // Set the record ID
            stmt.executeUpdate(); // Execute the delete query

        } catch (Exception e) {
            // Print an error message if the deletion fails
            System.err.println("Error deleting health record:");
            e.printStackTrace();
        }
    }
}








