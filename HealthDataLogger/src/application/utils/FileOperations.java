package application.utils;

import java.io.*;
import java.io.IOException;
import java.util.*;

import javafx.scene.control.CheckBox;

public class FileOperations {
	public static void writeCredentialsToFile(String userID, String password, CheckBox tech) throws IOException {
		FileOutputStream fos = new FileOutputStream("Test 1.txt", true);
		DataOutputStream dos = new DataOutputStream(fos);
		
		dos.writeChars(userID+","+password+","+tech+"\n");
		
		dos.close();
		fos.close();
	}
	
	public static List<String[]> readCredentialsFromFile() throws IOException {
		String line;
		List<String[]> credentialList = new ArrayList<>();
		
		FileInputStream fis = new FileInputStream("Test 1.txt");
		DataInputStream dis = new DataInputStream(fis);
		
		while(dis.available()>0) {
			line = dis.readUTF();
			
			String[] credentials = line.split(",");
			
			String userID = credentials[0];
			String password = credentials[1];
			
			credentialList.add(new String[] {userID, password});
		}
		
		dis.close();
		fis.close();
		
		return credentialList;
	}
}