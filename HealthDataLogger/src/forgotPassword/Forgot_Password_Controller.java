package forgotPassword;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		verifyCode.setVisible(false);
		newPassword.setVisible(false);
		confirmPassword.setVisible(false);
		promptLabel.setVisible(false);
		
		uniButton.setText("Send Code");
	}
	
	private ArrayList<Integer> codeList;
	private int code;
	
	Random rand = new Random();
	
	// Email pattern
	private boolean isValid(String text) {
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		return Pattern.matches(regex, text);
	}
	
	// Alert Method
	private void showAlert(String title, String message) {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
		alert.setTitle(title);
		alert.showAndWait();
	}
	
	// Generate a code upon clicking Uni-button
	private int generateVerificationCode() {
		// TODO Auto-generated method stub
		return 100000 + rand.nextInt(900000);
	}
	
	// Verify the input code
	public boolean codeVerification(int a) {
		return codeList.contains(a);
	}
	
	// Function to verify email and send code
	public void sendCode() {
		uniButton.setOnAction(e -> {
			System.out.println("Send Code Clicked");
//			navigateToMain();
		});
		try {
			
			if(isValid(userID.getText())) {
				code = generateVerificationCode();
				codeList.add(code);
				System.out.println(code);
				verifyCode.setVisible(true);
				uniButton.setText("Verify Code");
			} else {
				promptLabel.setText("Invalid Email ID");
				promptLabel.setVisible(true);
			}
		} catch (Exception e) {
			showAlert("Error", "Invalid information: "+e.getMessage());			
		}
	}
	
	// Function to check code
	public void checkCode() {
		try {
			int a = Integer.parseInt(verifyCode.getText());
			
			if(codeVerification(a))
			{
				newPassword.setVisible(true);
				confirmPassword.setVisible(true);
				uniButton.setText("Reset Password");
				
				String pass = newPassword.getText();
				
				promptLabel.setVisible(false);
				
				uniButton.setOnAction(x -> {
					if(pass == confirmPassword.getText())
					{					
						promptLabel.setText("Password Successfully Reset");
						promptLabel.setVisible(true);
						
						navigateToMain();
					}
					else
					{
						promptLabel.setText("Password fails to Match!");
						promptLabel.setVisible(true);
					}
				});
			}
			else
			{
				promptLabel.setText("Incorrect Code!");
				promptLabel.setVisible(true);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			promptLabel.setText("Enter Only Digits!");
			promptLabel.setVisible(true);
		}
	}
	
	// Function to navigate back to Main Window
	public void navigateToMain() {
		try {
			
			//Loading login screen
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/forgotPassword/Forgot_Password.fxml"));
			// Scene loginScene = new Scene(loader.load());
			Parent loginScene = loader.load();
			
			// Getting current Stage
			Stage currentStage = (Stage) uniButton.getScene().getWindow();
			
			//Closing the current window
			currentStage.close();
			
			// New Stage to go back to existing window
			Stage main = new Stage();
			main.setScene(new Scene(loginScene));
			main.show();
			
		} catch (Exception a) {
			a.printStackTrace();
			showAlert("Error", "Failed to load the login screen");
		}
	}
}