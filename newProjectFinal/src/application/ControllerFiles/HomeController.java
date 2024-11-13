package application.ControllerFiles;
import java.net.URL;
import java.util.ResourceBundle;
import application.utils.SwitchScenes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeController implements Initializable {
	
	@FXML
	private Button createNewEntry,modify, myAcc;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createNewEntry.setOnAction(event -> gotoNewEntryWindow());
		
		Platform.runLater(() -> {
			Stage currentStage = (Stage) createNewEntry.getScene().getWindow();
			currentStage.centerOnScreen();
			currentStage.setMaximized(true);
		});
	}
	
	public void gotoNewEntryWindow() {
		System.out.println("Create New Entry Clicked");
		Stage currentStage = (Stage) createNewEntry.getScene().getWindow();
    	SwitchScenes.switchScene("/healthDataRecords/HealthDataRecords.fxml", "Health Data Record", currentStage);
	}
}