package application.utils;

import java.util.regex.Pattern;

public class Validators {
	
	// Hard coded username and password
	public static final String CORRECT_USERNAME = "admin";
	public static final String CORRECT_PASSWORD = "pass123";
	
	// Email pattern
	public static boolean isValid(String text) {
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		return Pattern.matches(regex, text);
	}
		
	// Password Match
	public static boolean passCheck(String p1, String p2) {
		return p1 == p2;
	}
}
