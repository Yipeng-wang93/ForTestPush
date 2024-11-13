package technicianHome;

import javafx.collections.FXCollections; // Imports utility for creating observable lists
import javafx.collections.ObservableList; // Provides list that can be observed for changes
import javafx.fxml.FXML; // Annotation for FXML-injected fields and methods
import javafx.scene.control.*; // Imports all JavaFX UI controls
import javafx.scene.control.cell.PropertyValueFactory; // Binds properties to TableView columns
import startingPoint.Main; // Reference to the main application
import java.time.LocalDate; // Utility for handling date selection

public class TechnicianHomeController {

    private Main mainApp; // Reference to the main application

    @FXML
    private MenuBar techHomeMenuBar; // MenuBar UI element

    @FXML
    private TreeView<String> techHomeTreeView; // TreeView for navigation options

    @FXML
    private TextField techHomeUserNameOrIdTextField; // TextField for username or ID input

    @FXML
    private DatePicker techHomeDatePicker; // DatePicker for filtering by date

    @FXML
    private ComboBox<String> techHomeGenderComboBox; // ComboBox for gender selection

    @FXML
    private Button techHomeSearchButton; // Search button for filtering records

    @FXML
    private Button techHomeResetButton; // Reset button to clear filters

    @FXML
    private Button techHomeLogOutButton; // Log Out button for navigation

    @FXML
    private Button techHomeViewButton; // View button to open selected record

    @FXML
    private TableView<Record> techHomeResultsTable; // TableView to display search results

    @FXML
    private TableColumn<Record, String> techHomeRecordIdColumn; // Column to display record ID

    @FXML
    private TableColumn<Record, String> techHomePatientIdColumn; // Column to display patient ID

    @FXML
    private TableColumn<Record, String> techHomeUserNameColumn; // Column to display username

    @FXML
    private TableColumn<Record, String> techHomeEntryCreatedTimeColumn; // Column for creation date

    @FXML
    private TableColumn<Record, String> techHomeEntryUpdatedTimeColumn; // Column for last update date

    private ObservableList<Record> records = FXCollections.observableArrayList(); // List to store record data

    @FXML
    public void initialize() {
        // Set items for gender ComboBox
        techHomeGenderComboBox.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));

        // Initialize TreeView with navigation options
        TreeItem<String> rootItem = new TreeItem<>("Options");
        rootItem.setExpanded(true); // Expands the root item by default
        TreeItem<String> technicianHomeItem = new TreeItem<>("Technician Home");
        TreeItem<String> patientAccountManagementItem = new TreeItem<>("Patient Account Management");
        TreeItem<String> myAccountItem = new TreeItem<>("My Account");
        rootItem.getChildren().addAll(technicianHomeItem, patientAccountManagementItem, myAccountItem);
        techHomeTreeView.setRoot(rootItem); // Set root for TreeView

        // Add double-click listener to TreeView for navigation
        techHomeTreeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Check if double-clicked
                TreeItem<String> selectedItem = techHomeTreeView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    String itemName = selectedItem.getValue();
                    if ("Patient Account Management".equals(itemName)) {
                        mainApp.loadPatientAccountManagement(); // Navigate to Patient Account Management
                    } else if ("Technician Home".equals(itemName)) {
                        mainApp.loadTechnicianHome(); // Reload Technician Home
                    } else if ("My Account".equals(itemName)) {
                        System.out.println("Navigating to My Account (Placeholder for future integration).");
                        // Placeholder for future integration of "My Account" by others
                    }
                }
            }
        });

        // Bind TableView columns to properties in Record class
        techHomeRecordIdColumn.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        techHomePatientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        techHomeUserNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        techHomeEntryCreatedTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createdTime"));
        techHomeEntryUpdatedTimeColumn.setCellValueFactory(new PropertyValueFactory<>("updatedTime"));

        // Load initial data into TableView
        loadRecords();
        techHomeResultsTable.setItems(records); // Set the records list to TableView
    }

    // Method to load sample records into records list
    private void loadRecords() {
        records.addAll(
            new Record("00001", "12345", "Yipeng Wang", "2024-10-11", "2023-11-11"),
            new Record("00002", "13456", "Narendra", "2023-10-12", "2023-11-12")
        );
    }

    // Event handler for the "Search" button
    @FXML
    private void onSearch() {
        String userNameOrId = techHomeUserNameOrIdTextField.getText(); // Get username or ID input
        LocalDate selectedDate = techHomeDatePicker.getValue(); // Get selected date
        String selectedGender = techHomeGenderComboBox.getValue(); // Get selected gender

        ObservableList<Record> filteredRecords = FXCollections.observableArrayList(); // List for filtered records
        for (Record record : records) {
            // Filter based on input criteria
            boolean matchesUserNameOrId = userNameOrId.isEmpty() || record.getUserName().contains(userNameOrId) || record.getPatientId().contains(userNameOrId);
            boolean matchesDate = selectedDate == null || record.getCreatedTime().equals(selectedDate.toString());
            boolean matchesGender = selectedGender == null || selectedGender.isEmpty() || record.getUserName().equalsIgnoreCase(selectedGender);

            if (matchesUserNameOrId && matchesDate && matchesGender) {
                filteredRecords.add(record); // Add matching records to filtered list
            }
        }
        techHomeResultsTable.setItems(filteredRecords); // Display filtered records in TableView
    }

    // Event handler for the "Reset" button
    @FXML
    private void onReset() {
        techHomeUserNameOrIdTextField.clear(); // Clear username or ID field
        techHomeDatePicker.setValue(null); // Reset date picker
        techHomeGenderComboBox.getSelectionModel().clearSelection(); // Clear ComboBox selection
        techHomeResultsTable.setItems(records); // Reset TableView to show all records
    }

    // Event handler for the "View" button
    @FXML
    private void onViewHealthData() {
        Record selectedRecord = techHomeResultsTable.getSelectionModel().getSelectedItem(); // Get selected record
        if (selectedRecord != null) {
            mainApp.loadHealthDataRecords(
                selectedRecord.getUserName(),
                "30",  // Sample data for age
                "Male",  // Sample data for gender
                "O+",  // Sample data for blood type
                "180 cm",  // Sample data for height
                "80 kg"  // Sample data for weight
            );
        } else {
            System.out.println("Please select a record to view."); // Notify if no record is selected
        }
    }

    // Event handler for the "Log Out" button
    @FXML
    private void onLogOut() {
        System.out.println("Logging out... (Placeholder for future integration with Login screen)");
        // Placeholder for future integration of "Login" screen by others
    }

    // Inner class representing a record in the table
    public static class Record {
        private final String recordId; // Record ID field
        private final String patientId; // Patient ID field
        private final String userName; // Username field
        private final String createdTime; // Account creation date
        private final String updatedTime; // Last update date

        // Constructor to initialize all fields
        public Record(String recordId, String patientId, String userName, String createdTime, String updatedTime) {
            this.recordId = recordId;
            this.patientId = patientId;
            this.userName = userName;
            this.createdTime = createdTime;
            this.updatedTime = updatedTime;
        }

        // Getter methods for record details
        public String getRecordId() { return recordId; }
        public String getPatientId() { return patientId; }
        public String getUserName() { return userName; }
        public String getCreatedTime() { return createdTime; }
        public String getUpdatedTime() { return updatedTime; }
    }

    // Setter to link main application reference
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}













