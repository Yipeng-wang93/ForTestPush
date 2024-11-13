package application.utils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LogGenerator {
	
	public static void generateLog(Button button) {
		System.out.println(button.getText()+" Clicked");
	}
	
	public static void generateLog(Label label) {
		System.out.println(label.getText()+" Clicked");
	}
}
