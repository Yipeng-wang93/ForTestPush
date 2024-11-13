package startingPoint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import technicianHome.TechnicianHomeController;
import patientAccountManagement.PatientAccountManagementController;
import healthDataRecords.HealthDataRecordsController;

public class Main extends Application {
    private static Stage primaryStage;
    private static Main instance;
    // private HealthDataRecordsController healthDataRecordsController;

    @Override
    public void start(Stage stage) {
        instance = this;
//        primaryStage = stage;
//        loadTechnicianHome(); // Start with Technician Home view
    }

    public static Main getInstance() {
        return instance;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    // Method to load Technician Home FXML
    public void loadTechnicianHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/technicianHome/TechnicianHome.fxml"));
            Parent root = loader.load();

            // Get the controller and set the mainApp reference
            TechnicianHomeController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setTitle("Technician Home");
            primaryStage.setScene(new Scene(root, 700, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading TechnicianHome.fxml");
        }
    }

    // Method to load Patient Account Management FXML
    public void loadPatientAccountManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/patientAccountManagement/PatientAccountManagement.fxml"));
            Parent root = loader.load();

            // Get the controller and set the mainApp reference
            PatientAccountManagementController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setTitle("Patient Account Management");
            primaryStage.setScene(new Scene(root, 700, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading PatientAccountManagement.fxml");
        }
    }

    // Method to load Health Data Records FXML
    public void loadHealthDataRecords(String name, String age, String gender, String bloodType, String height, String weight) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/healthDataRecords/HealthDataRecords.fxml"));
            Parent root = loader.load();

            // Get the controller and set the mainApp reference
            HealthDataRecordsController controller = loader.getController();
            controller.setMainApp(this);
            controller.setPatientInfo(name, age, gender, bloodType, height, weight);
            
            /*// Load data from file at the start of Health Data Records view
            controller.loadDataFromFile();
            // Save reference to use in stop() for data saving
            this.healthDataRecordsController = controller; */

            primaryStage.setTitle("Health Data Records");
            primaryStage.setScene(new Scene(root, 700, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading HealthDataRecords.fxml");
        }
    }
    
     // @Override
    /*public void stop() {
        // Save data to file if healthDataRecordsController was loaded
        if (healthDataRecordsController != null) {
            healthDataRecordsController.saveDataToFile();
        }
    } */

    public static void main(String[] args) {
        launch(args);
    }
}







