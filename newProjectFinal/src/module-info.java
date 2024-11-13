module newProject {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
	opens application.ControllerFiles to javafx.fxml;
	
	//Yipeng Changes below
	
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
