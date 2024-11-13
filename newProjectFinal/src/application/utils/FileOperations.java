package application.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileOperations {

    public static void writeToAFile(String email, String password, String name, String isTech) {
        try (FileWriter fw = new FileWriter("Credentials.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(email + "|" + password + "|" + name + "|" + isTech);
            bw.newLine();
            System.out.println("File created successfully");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public static List<Credentials> readFromAFile() {
        List<Credentials> credentials = new ArrayList<>();
        File file = new File("Credentials.txt");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] lines = line.split("\\|");
                    if (lines.length == 4) {
                        credentials.add(new Credentials(lines[0], lines[1], lines[2], lines[3]));
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file.");
                e.printStackTrace();
            }
        }
        return credentials;
    }

    public static boolean passwordReset(String email, String newPassword) {
        List<Credentials> credentials = readFromAFile();
        boolean isUpdated = false;

        for (Credentials cred : credentials) {
            if (cred.getEmailID().equalsIgnoreCase(email)) {
                cred.setPassword(newPassword);
                isUpdated = true;
                break;
            }
        }

        if (isUpdated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Credentials.txt"))) {
                for (Credentials cred : credentials) {
                    writer.write(cred.getEmailID() + "|" + cred.getPassword() + "|" + cred.getName() + "|" + cred.getIsTech());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    public static void fileLogs(String text) {
        try (FileWriter fw = new FileWriter("Log.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(text + " Clicked");
            bw.newLine();
            System.out.println("File created successfully");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the log file.");
            e.printStackTrace();
        }
    }
}