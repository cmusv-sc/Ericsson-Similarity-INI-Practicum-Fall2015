package com.cmu.model;

public class User {
          
    private String login;
     
    private String password;
     
    private String role;
    
    public User(String username, String password, String role) {
    	this.login = username;
    	this.password = password;
    	this.role = role;

	}
 
    public User() {
		// TODO Auto-generated constructor stub
	}

	public String getLogin() {
        return login;
    }
 
    public void setLogin(String login) {
        this.login = login;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public String getRole() {
        return role;
    }
 
    public void setRole(String role) {
        this.role = role;
    }   
 
}