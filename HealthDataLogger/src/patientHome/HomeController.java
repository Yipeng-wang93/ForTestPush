package patientHome;

//import application.utils.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HomeController implements Initializable {
	
	@FXML
	private Button createNewEntry,modify, myAcc;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createNewEntry.setOnAction(event -> gotoNewEntryWindow());
	}
	
	public void gotoNewEntryWindow() {
		System.out.println("Create New Entry Clicked");
//		Stage currentStage = ;
		createNewEntry.setOnAction(e -> {
			
			Stage primaryStage = new Stage();
			BorderPane root;
			try {
				root = (BorderPane)FXMLLoader.load(getClass().
						getResource("/patientHome/Home.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setMaximized(true);
				primaryStage.setTitle("Create New Entry");
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
		});
	}
}
