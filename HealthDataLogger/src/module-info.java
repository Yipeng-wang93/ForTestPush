module HealthDataLogger {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
    //requires com.fasterxml.jackson.databind;
    //requires com.fasterxml.jackson.core;
    //requires com.fasterxml.jackson.annotation;


    // Export the main package to allow JavaFX to access the Application class
    exports startingPoint;

    // Export other packages containing controllers, if needed by other modules
    exports technicianHome;
    exports patientAccountManagement;
    exports healthDataRecords;

    // Open packages to javafx.fxml to allow reflection for FXML loading
    opens technicianHome to javafx.fxml;
    opens patientAccountManagement to javafx.fxml;
    opens healthDataRecords to javafx.fxml;
}


