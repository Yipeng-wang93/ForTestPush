package application.utils;

public class Credentials {
	
    private String emailID;
    private String password;
    private String name;
    private String isTech;
    
    public String getEmailID() {
    	return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPassword() {
    	return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
    	return name;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public String getIsTech() {
    	return isTech;
    }

    public void setIsTech(String isTech) {
    	this.isTech = isTech;
    }

    public Credentials(String emailID, String password, String name, String isTech) {
    	this.emailID = emailID;
        this.password = password;
        this.name = name;
        this.isTech = isTech;
    }
}