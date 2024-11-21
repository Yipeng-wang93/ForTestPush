package login;

import java.net.URL;
import java.util.ResourceBundle;

import application.utils.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import startingPoint.Main;

public class LoginController implements Initializable {
	
	private Main mainApp;
	
	// Text field for User's Email-ID
	@FXML
	private TextField userID;
	
	// Password Field for user to enter their Password
	@FXML
	private PasswordField password;
	
	// Label for Prompting invalid entry and Forgot password
	@FXML
	private Label forgotPassword, promptLabel, accountCreation;
	
	// Login button
	@FXML
	private Button logIn;
	
	// Checkbox for remember login credentials
	@FXML
	private CheckBox rememberMe;

	@Override
	public void initialize (URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		forgotPassword.setCursor(Cursor.HAND);
		promptLabel.setVisible(false);
		
		logIn.setOnAction(event -> login());
		forgotPassword.setOnMouseClicked(event -> forgotPassword());
		accountCreation.setOnMouseClicked(event -> createAccount());
	}
    
    public void login() {    	
    	logIn.setOnAction(event -> {
    		LogGenerator.generateLog(logIn);
            String enteredUsername = userID.getText();
            String enteredPassword = password.getText();

            if (enteredUsername.equals(Validators.CORRECT_USERNAME) && enteredPassword.equals(Validators.CORRECT_PASSWORD)) {
            	navigateToHomeScreen();
            } else {
            	promptLabel.setText("Login failed.\nPlease check your credentials.");
            	promptLabel.setVisible(true);
            }
        });
    }
    
    private void navigateToHomeScreen() {
    	Stage currentStage = (Stage) logIn.getScene().getWindow();
    	SwitchScenes.switchScene("/src/patientHome/Home.fxml", "PatientHome", currentStage);
	}

	public void forgotPassword() {
		LogGenerator.generateLog(forgotPassword);
		Stage currentStage = (Stage) forgotPassword.getScene().getWindow();
		SwitchScenes.switchScene("/src/forgotPassword/Forgot_Password.fxml", "Forgot Password", currentStage);
    }

	public void createAccount() {
		LogGenerator.generateLog(accountCreation);
		Stage currentStage = (Stage) accountCreation.getScene().getWindow();
		SwitchScenes.switchScene("/src/accountCreation/AccountCreation.fxml", "Create Account", currentStage);		
    }
	
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
