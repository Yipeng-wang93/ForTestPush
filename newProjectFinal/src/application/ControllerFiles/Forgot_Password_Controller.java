package application.ControllerFiles;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import application.utils.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Forgot_Password_Controller implements Initializable {
	
	// Text fields for userID and verification code
	@FXML
	private TextField userID, verifyCode;
	
	//Password Fields for new password and confiming new password
	@FXML
	private PasswordField newPassword, confirmPassword;
	
	// Prompt Label for invalid entry
	@FXML
	private Label promptLabel;
	
	// Single button where text changes upon meeting specific conditions
	@FXML
	private Button uniButton;
	
	private int code; 
	private List<Integer> codes = new ArrayList<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		verifyCode.setVisible(false);
		newPassword.setVisible(false);
		confirmPassword.setVisible(false);
		promptLabel.setVisible(false);
		
		uniButton.setText("Send Code");
		
		uniButton.setOnAction(e -> resetPassword());
		
		Platform.runLater(() -> {
			Stage currentStage = (Stage) uniButton.getScene().getWindow();
			currentStage.centerOnScreen();
			currentStage.setMaximized(false);
		});
	}
	
	public int generateVerificationCode() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
	
	@FXML
	public void resetPassword() {
			try {
				
				promptLabel.setText("");
				promptLabel.setVisible(false);
				
				if(!Validators.emailAvailability(userID.getText())) {
					promptLabel.setText("Email does not Exists!");
					promptLabel.setVisible(true);
					return;
				}
				
				if(uniButton.getText().equalsIgnoreCase("Send Code")) {
					code = generateVerificationCode();
					codes.add(code);
					System.out.println(code);
					
					uniButton.setText("Verify Code");
					verifyCode.setVisible(true);
					return;
				}
				
				if(uniButton.getText().equalsIgnoreCase("Verify Code")) {
					try {
						promptLabel.setText("");
						promptLabel.setVisible(false);
						
						if(codes.contains(Integer.parseInt(verifyCode.getText()))) {
							promptLabel.setText("Code Verified Successfully!");
							newPassword.setVisible(true);
							confirmPassword.setVisible(true);
							
							uniButton.setText("Reset Password");
						} else {
							promptLabel.setText("Incorrect verification code.");
							promptLabel.setVisible(true);
							verifyCode.clear();
						}
						
					} catch (NumberFormatException n) {
						promptLabel.setText("Invalid verification code format!");
					    promptLabel.setVisible(true);
					}
				}
				
				if(uniButton.getText().equalsIgnoreCase("Reset Password")) {
					
					promptLabel.setText("");
					promptLabel.setVisible(false);
					if(Validators.emptyFieldCheck(newPassword)) {
						promptLabel.setText("Enter Password");
						promptLabel.setVisible(true);
						return;
					}
					
					if(Validators.emptyFieldCheck(confirmPassword)) {
						promptLabel.setText("Enter Confirm Password");
						promptLabel.setVisible(true);
						return;
					}
					
					if(!Validators.passCheck(newPassword.getText(), confirmPassword.getText())) {
						promptLabel.setText("Password Mismatches");
						promptLabel.setVisible(true);
					} else {
						FileOperations.passwordReset(userID.getText(), newPassword.getText());
						Stage currentStage = (Stage) uniButton.getScene().getWindow();
	                	SwitchScenes.switchScene("/application/DesignFiles/Main.fxml", "Login", currentStage);
					}
				}
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}
}