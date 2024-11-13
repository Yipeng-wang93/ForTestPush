package healthDataRecords;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TreeItem;
import startingPoint.Main;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.File;
//import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HealthDataRecordsController {

    private Main mainApp; // Reference to the main application for navigation
    // private final String DATA_FILE_PATH = "healthMetricsData.json"; // File path for data storage (commented out for future use)

    @FXML
    private MenuBar healthDataRecordsMenuBar; // Menu bar for health data records screen

    @FXML
    private TreeView<String> healthMetricsTreeView; // TreeView for displaying health metrics categories and items

    @FXML
    private TextField patientNameTextField; // Text field to display patient's name

    @FXML
    private TextField ageTextField; // Text field to display patient's age

    @FXML
    private TextField genderTextField; // Text field to display patient's gender

    @FXML
    private TextField bloodTypeTextField; // Text field to display patient's blood type

    @FXML
    private TextField heightTextField; // Text field to display patient's height

    @FXML
    private TextField weightTextField; // Text field to display patient's weight

    @FXML
    private TextArea healthDataDisplayTextArea; // Text area for displaying selected health metric details

    @FXML
    private Button hDRModifyButton; // Button for modifying health metric data

    @FXML
    private Button hDRSaveButton; // Button for saving modified health metric data

    @FXML
    private Button hDRDeleteButton; // Button for deleting health metric data

    @FXML
    private Button hDRBackToTechnicianHomeButton; // Button to return to Technician Home

    private boolean isEditable = false; // Flag to check if data is currently editable

    // Placeholder for health metrics data storage
    private Map<String, String> healthMetricsData;

    @FXML
    public void initialize() {
        // Initialize the TreeView structure
        initializeTreeView();
        // Load default patient information
        loadPatientInfo();
        // Load sample data for health metrics
        loadSampleHealthMetricsData();
        // Setup a listener for changes in TreeView selection
        setupTreeViewListener();
    }

    // Method to initialize the TreeView with categories and items
    private void initializeTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("Health Metrics"); // Root item of TreeView
        rootItem.setExpanded(true); // Expand root item

        // Create "Blood Metrics" category with specific items
        TreeItem<String> bloodMetrics = new TreeItem<>("Blood Metrics");
        bloodMetrics.getChildren().addAll(
            new TreeItem<>("Blood Glucose"),
            new TreeItem<>("Leucocyte Count"),
            new TreeItem<>("Erythrocyte Count"),
            new TreeItem<>("Blood Oxygen"),
            new TreeItem<>("Blood Pressure"),
            new TreeItem<>("Body Temperature")
        );

        // Create "Heart Metrics" category with specific items
        TreeItem<String> heartMetrics = new TreeItem<>("Heart Metrics");
        heartMetrics.getChildren().addAll(
            new TreeItem<>("Heart Rate"),
            new TreeItem<>("Cholesterol Level"),
            new TreeItem<>("Coronary Artery Calcium")
        );

        // Create "Respiratory Metrics" category with specific items
        TreeItem<String> respiratoryMetrics = new TreeItem<>("Respiratory Metrics");
        respiratoryMetrics.getChildren().addAll(
            new TreeItem<>("Respiratory Rate"),
            new TreeItem<>("Vital Capacity")
        );

        // Add categories to the root item
        rootItem.getChildren().addAll(bloodMetrics, heartMetrics, respiratoryMetrics);
        healthMetricsTreeView.setRoot(rootItem); // Set root item for TreeView
    }

    // Method to load patient information into text fields
    private void loadPatientInfo() {
        setPatientInfo("Yipeng Wang", "30", "Male", "O+", "180 cm", "80 kg"); // Set sample data
        healthDataDisplayTextArea.setText("Select a health metric from the list to view details."); // Default message in TextArea
    }

    // Method to load sample health metrics data into the map
    private void loadSampleHealthMetricsData() {
        healthMetricsData = new HashMap<>(); // Initialize map
        healthMetricsData.put("Blood Glucose", "Blood Glucose Level: 100 mg/dL");
        healthMetricsData.put("Leucocyte Count", "Leucocyte Count: 6800 cells/mcL");
        healthMetricsData.put("Erythrocyte Count", "Erythrocyte Count: 4 million cells/mcL");
        healthMetricsData.put("Blood Oxygen", "Blood Oxygen Level: 97%");
        healthMetricsData.put("Blood Pressure", "Blood Pressure: 130/70 mmHg");
        healthMetricsData.put("Body Temperature", "Body Temperature: 96.6°F");
        healthMetricsData.put("Heart Rate", "Heart Rate: 72 bpm");
        healthMetricsData.put("Cholesterol Level", "Cholesterol Level: 190 mg/dL");
        healthMetricsData.put("Coronary Artery Calcium", "Coronary Artery Calcium: 310 Agatston units");
        healthMetricsData.put("Respiratory Rate", "Respiratory Rate: 15 breaths/min");
        healthMetricsData.put("Vital Capacity", "Vital Capacity: 5 liters");
    }

    // Method to set up a listener for changes in TreeView selection
    private void setupTreeViewListener() {
        healthMetricsTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { // If an item is selected
                String selectedMetric = newValue.getValue(); // Get selected metric name
                displayHealthMetricData(selectedMetric); // Display data for selected metric
                isEditable = false; // Set to non-editable
                healthDataDisplayTextArea.setEditable(false); // Disable editing in TextArea
            }
        });
    }

    // Method to display health metric data in TextArea
    private void displayHealthMetricData(String metric) {
        String data = healthMetricsData.getOrDefault(metric, "No data available for " + metric); // Get metric data or default message
        healthDataDisplayTextArea.setText(data); // Set data in TextArea
        System.out.println("Displaying data for: " + metric); // Log display action
    }

    // Event handler for "Modify" button
    @FXML
    private void onModifyData() {
        if (healthMetricsTreeView.getSelectionModel().getSelectedItem() != null) { // Check if a metric is selected
            isEditable = true; // Allow editing
            healthDataDisplayTextArea.setEditable(true); // Enable TextArea for editing
            System.out.println("Modify button clicked - data is now editable."); // Log action
        } else {
            showAlert("No Selection", "Please select a health metric to modify."); // Show alert if no metric is selected
        }
    }

    // Event handler for "Save" button
    @FXML
    private void onSaveData() {
        if (isEditable) { // Check if editing is allowed
            String modifiedData = healthDataDisplayTextArea.getText(); // Get modified data from TextArea
            String selectedMetric = healthMetricsTreeView.getSelectionModel().getSelectedItem().getValue(); // Get selected metric name

            if (validateMetricInput(selectedMetric, modifiedData)) { // Validate input data
                healthMetricsData.put(selectedMetric, modifiedData); // Save modified data in map
                healthDataDisplayTextArea.setEditable(false); // Set TextArea to non-editable
                isEditable = false; // Reset edit flag
                System.out.println("Save button clicked - data saved: " + modifiedData); // Log save action
            }
        } else {
            showAlert("Modify First", "Please click 'Modify' to enable editing."); // Show alert if data is not editable
        }
    }

    // Method to validate input data based on the metric type
    private boolean validateMetricInput(String metric, String value) {
        try {
            double numericValue = Double.parseDouble(value.replaceAll("[^0-9.]", "")); // Extract numeric value
            switch (metric) {
                case "Blood Glucose":
                    if (numericValue < 70 || numericValue > 140) {
                        showAlert("Invalid Input", "Blood Glucose must be between 70 and 140 mg/dL.");
                        return false;
                    }
                    break;
                case "Blood Pressure":
                    if (numericValue < 90 || numericValue > 180) {
                        showAlert("Invalid Input", "Blood Pressure must be between 90 and 180 mmHg.");
                        return false;
                    }
                    break;
                case "Body Temperature":
                    if (numericValue < 95 || numericValue > 99) {
                        showAlert("Invalid Input", "Body Temperature must be between 95°F and 99°F.");
                        return false;
                    }
                    break;
                case "Heart Rate":
                    if (numericValue < 60 || numericValue > 100) {
                        showAlert("Invalid Input", "Heart Rate must be between 60 and 100 bpm.");
                        return false;
                    }
                    break;
                default:
                    break;
            }
            return true; // Validation passed
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid numeric value for " + metric); // Show alert if input is invalid
            return false;
        }
    }

    // Method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Create error alert
        alert.setTitle(title); // Set alert title
        alert.setHeaderText(null); // Remove header text
        alert.setContentText(message); // Set alert message
        alert.showAndWait(); // Display alert
    }

    // Event handler for "Delete" button
    @FXML
    private void onDeleteData() {
        healthDataDisplayTextArea.clear(); // Clear TextArea
        System.out.println("Delete button clicked - data cleared."); // Log action
    }

    // Event handler for "Back" button
    @FXML
    private void onBack() {
        mainApp.loadTechnicianHome(); // Navigate back to Technician Home view
        System.out.println("Back button clicked - returning to Technician Home."); // Log navigation
    }

    // Method to set patient information in the respective TextFields
    public void setPatientInfo(String name, String age, String gender, String bloodType, String height, String weight) {
        patientNameTextField.setText(name != null ? name : "Unknown");
        ageTextField.setText(age != null ? age : "Unknown");
        genderTextField.setText(gender != null ? gender : "Unknown");
        bloodTypeTextField.setText(bloodType != null ? bloodType : "Unknown");
        heightTextField.setText(height != null ? height : "Unknown");
        weightTextField.setText(weight != null ? weight : "Unknown");
    }

    // Method to set the main application reference
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    // Methods to load and save data to a file for persistence (commented out for future use)
    /*
    public void loadDataFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(DATA_FILE_PATH);
            if (file.exists()) {
                healthMetricsData = mapper.readValue(file, HashMap.class);
                System.out.println("Data loaded from file.");
            } else {
                healthMetricsData = new HashMap<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveDataToFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(DATA_FILE_PATH), healthMetricsData);
            System.out.println("Data saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}







