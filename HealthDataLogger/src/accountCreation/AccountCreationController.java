package accountCreation;

import java.net.URL;
import java.util.ResourceBundle;
//import java.util.regex.Pattern;

import application.utils.FileOperations;
import application.utils.Validators;
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
				
				if((Validators.isValid(userMailID.getText()))) {
					promptLabel.setText("Please Enter Valid Email\nOr Password Mismatch!");
					promptLabel.setVisible(true);
				} else {
					promptLabel.setText("Account Created");
					promptLabel.setVisible(true);
					FileOperations.writeCredentialsToFile(userMailID.getText(), password1.getText(), isATech);
				}
			} catch (Exception x) {
				showAlert("Error", "Invalid information: "+x.getMessage());			
			}
	}
	
	public void AccCreate() {
		createAccount.setOnAction(e -> {
            String username = userMailID.getText();
            String password = password1.getText();
            String passwordA = password2.getText();
            
            if (username.isEmpty() || password.isEmpty() && Validators.passCheck(password, passwordA)) {
            	promptLabel.setText("All fields are required.");
            	promptLabel.setVisible(true);
            } else {
            	promptLabel.setText("Account created for " + username + "!");
            	promptLabel.setVisible(true);
            }
        });
	}
}
