package patientAccountManagement;

import application.utils.SwitchScenes; // Import utility for scene switching
import javafx.beans.property.SimpleStringProperty; // Provides a simple string property for data binding
import javafx.beans.property.StringProperty; // Interface for a string property for data binding
import javafx.collections.FXCollections; // Utility for creating observable lists
import javafx.collections.ObservableList; // List that can be observed for changes
import javafx.fxml.FXML; // Annotation for FXML injected fields and methods
import javafx.scene.control.*; // Imports all controls for UI elements
import startingPoint.Main; // Import main application reference

public class PatientAccountManagementController {

    private Main mainApp; // Reference to the main application for navigation

    @FXML
    private TreeView<String> patientAMTreeView; // TreeView to display navigation options

    @FXML
    private TextField patientAMUserNameOrIdTextField; // TextField for entering username or ID

    @FXML
    private DatePicker patientAMDatePicker; // DatePicker for filtering by account creation date

    @FXML
    private ComboBox<String> patientAMRegionComboBox; // ComboBox for selecting a region

    @FXML
    private TableView<Record> patientAMTableView; // TableView to display records of patients
    @FXML
    private TableColumn<Record, String> patientAMRecordIdColumn; // Column for displaying record ID
    @FXML
    private TableColumn<Record, String> patientAMUserNameColumn; // Column for displaying username
    @FXML
    private TableColumn<Record, String> patientAMRoleColumn; // Column for displaying role
    @FXML
    private TableColumn<Record, String> patientAMAccCreatedTimeColumn; // Column for displaying account creation date
    @FXML
    private TableColumn<Record, String> patientAMAccUpdatedTimeColumn; // Column for displaying last account update
    @FXML
    private TableColumn<Record, String> patientAMStatusColumn; // Column for displaying account status

    private ObservableList<Record> records = FXCollections.observableArrayList(); // Observable list for storing records

    @FXML
    public void initialize() {
        // Set up TreeView with navigation options
        TreeItem<String> rootItem = new TreeItem<>("Options");
        rootItem.setExpanded(true); // Expand the root item by default
        TreeItem<String> technicianHomeItem = new TreeItem<>("Technician Home");
        TreeItem<String> patientAccountManagementItem = new TreeItem<>("Patient Account Management");
        TreeItem<String> myAccountItem = new TreeItem<>("My Account");
        rootItem.getChildren().addAll(technicianHomeItem, patientAccountManagementItem, myAccountItem);
        patientAMTreeView.setRoot(rootItem); // Set root for TreeView

        // Handle double-click events on TreeView for navigation
        patientAMTreeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<String> selectedItem = patientAMTreeView.getSelectionModel().getSelectedItem(); // Get selected item
                if (selectedItem != null) {
                    switch (selectedItem.getValue()) {
                        case "Technician Home":
                            SwitchScenes.switchScene(null, null, null); // Switch scene using utility
                            mainApp.loadTechnicianHome(); // Navigate to Technician Home
                            break;
                    }
                }
            }
        });

        // Populate ComboBox with regions
        patientAMRegionComboBox.setItems(FXCollections.observableArrayList("Ontario", "Quebec", "British Columbia"));

        // Set up data binding for TableView columns
        patientAMRecordIdColumn.setCellValueFactory(cellData -> cellData.getValue().recordIdProperty());
        patientAMUserNameColumn.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
        patientAMRoleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        patientAMAccCreatedTimeColumn.setCellValueFactory(cellData -> cellData.getValue().accCreatedTimeProperty());
        patientAMAccUpdatedTimeColumn.setCellValueFactory(cellData -> cellData.getValue().accUpdatedTimeProperty());
        patientAMStatusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        loadRecords(); // Load initial records into the TableView
    }

    // Method to load sample records into the records list
    private void loadRecords() {
        records.addAll(
            new Record("00001", "Yipeng Wang", "Patient", "2023-12-10", "2023-12-12", "Active"),
            new Record("00002", "Narendra", "Technician", "2023-11-11", "2023-11-12", "Inactive")
        );
        patientAMTableView.setItems(records); // Set the records list to TableView
    }

    // Event handler for the "Search" button
    @FXML
    private void onSearch() {
        String userNameOrId = patientAMUserNameOrIdTextField.getText(); // Get username or ID input
        String selectedDate = (patientAMDatePicker.getValue() != null) ? patientAMDatePicker.getValue().toString() : ""; // Get date input if selected
        String selectedRegion = patientAMRegionComboBox.getValue(); // Get region input

        ObservableList<Record> filteredRecords = FXCollections.observableArrayList(); // List for filtered records
        for (Record record : records) {
            // Conditions to filter based on input values
            boolean matchesUserNameOrId = userNameOrId.isEmpty() || record.getUserName().contains(userNameOrId) || record.getRecordId().contains(userNameOrId);
            boolean matchesDate = selectedDate.isEmpty() || record.getAccCreatedTime().equals(selectedDate);
            boolean matchesRegion = selectedRegion == null || selectedRegion.isEmpty() || record.getRole().equals(selectedRegion);

            if (matchesUserNameOrId && matchesDate && matchesRegion) {
                filteredRecords.add(record); // Add matching records to the filtered list
            }
        }

        patientAMTableView.setItems(filteredRecords); // Display filtered records in TableView
        System.out.println("Search action triggered with filters - ID/Name: " + userNameOrId + ", Date: " + selectedDate + ", Region: " + selectedRegion);
    }

    // Event handler for the "Reset" button
    @FXML
    private void onReset() {
        patientAMUserNameOrIdTextField.clear(); // Clear username or ID field
        patientAMDatePicker.setValue(null); // Reset date picker
        patientAMRegionComboBox.getSelectionModel().clearSelection(); // Clear ComboBox selection
        patientAMTableView.setItems(records); // Reset TableView to show all records
    }

    // Event handler for the "View Account" button
    @FXML
    private void onViewAccount() {
        Record selectedRecord = patientAMTableView.getSelectionModel().getSelectedItem(); // Get selected record
        if (selectedRecord != null) {
            System.out.println("Switching to My Account view for: " + selectedRecord.getUserName());
            // Placeholder for integrating with the "My Account" view created by another team member
        } else {
            System.out.println("Please select a record to view."); // Notify if no record is selected
        }
    }

    // Setter for the main application reference
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    // Inner class representing a record in the table
    public static class Record {
        private final StringProperty recordId; // Property for record ID
        private final StringProperty userName; // Property for username
        private final StringProperty role; // Property for role
        private final StringProperty accCreatedTime; // Property for account creation time
        private final StringProperty accUpdatedTime; // Property for account update time
        private final StringProperty status; // Property for account status

        // Constructor for Record, initializing all properties
        public Record(String recordId, String userName, String role, String accCreatedTime, String accUpdatedTime, String status) {
            this.recordId = new SimpleStringProperty(recordId);
            this.userName = new SimpleStringProperty(userName);
            this.role = new SimpleStringProperty(role);
            this.accCreatedTime = new SimpleStringProperty(accCreatedTime);
            this.accUpdatedTime = new SimpleStringProperty(accUpdatedTime);
            this.status = new SimpleStringProperty(status);
        }

        // Property getter methods for data binding
        public StringProperty recordIdProperty() { return recordId; }
        public StringProperty userNameProperty() { return userName; }
        public StringProperty roleProperty() { return role; }
        public StringProperty accCreatedTimeProperty() { return accCreatedTime; }
        public StringProperty accUpdatedTimeProperty() { return accUpdatedTime; }
        public StringProperty statusProperty() { return status; }

        // Getter methods to retrieve values of properties
        public String getRecordId() { return recordId.get(); }
        public String getUserName() { return userName.get(); }
        public String getRole() { return role.get(); }
        public String getAccCreatedTime() { return accCreatedTime.get(); }
    }
}




