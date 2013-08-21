package com.example.model;

public class UserInfo {
	
	private int userID; 
	private String account;
	private String userName;
	private String password;
	
	private int getUserID() {
		return userID;
	}
	private void setUserID(int userID) {
		this.userID = userID;
	}
	private String getAccount() {
		return account;
	}
	private void setAccount(String account) {
		this.account = account;
	}
	private String getUserName() {
		return userName;
	}
	private void setUserName(String userName) {
		this.userName = userName;
	}
	private String getPassword() {
		return password;
	}
	private void setPassword(String password) {
		this.password = password;
	}


}
