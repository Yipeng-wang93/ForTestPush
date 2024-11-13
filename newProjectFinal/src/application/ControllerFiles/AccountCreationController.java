package application.ControllerFiles;

import java.net.URL;
import java.util.ResourceBundle;
//import java.util.regex.Pattern;

import application.utils.FileOperations;
import application.utils.SwitchScenes;
import application.utils.Validators;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AccountCreationController implements Initializable {
	
	@FXML
	private TextField userMailID, fullName;
	
	@FXML
	private PasswordField password1, password2;
	
	@FXML 
	private Button createAccount;
	
	@FXML
	private CheckBox isATech;
	
	@FXML
	private Label promptLabel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		promptLabel.setVisible(false);
		createAccount.setOnAction(e -> createAcc());
		
		Platform.runLater(() -> {
			Stage currentStage = (Stage) createAccount.getScene().getWindow();
			currentStage.centerOnScreen();
			currentStage.setMaximized(false);
		});
	}
		
	// Alert Method
	private void showAlert(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
		alert.setTitle(title);
		alert.showAndWait();
	}
	
	public void createAcc()
	{
		System.out.println("Create Account Button Clicked!");
			try {
				if((!Validators.isValid(userMailID.getText()))){
					promptLabel.setText("Please Enter Valid Email\nOr Password Mismatch!");
					promptLabel.setVisible(true);
				} else {
					String mail = userMailID.getText();
					String password = password1.getText();
					String confirmPassword = password2.getText();
					String name = fullName.getText();
					String isTech = Boolean.toString(isATech.isSelected());
					
					if(Validators.passCheck(password, confirmPassword)) {
						FileOperations.writeToAFile(mail, password, name, isTech);
						promptLabel.setText("Account Created");
						promptLabel.setVisible(true);
						
						Stage currentStage = (Stage) createAccount.getScene().getWindow();
						SwitchScenes.switchScene("/application/DesignFiles/Main.fxml", "Login", currentStage);
					} else {
						promptLabel.setText("Enter Password");
						promptLabel.setVisible(true);
					}
				}
			} catch (Exception x) {
				showAlert("Error", "Invalid information: "+x.getMessage());			
			}
	}
}
