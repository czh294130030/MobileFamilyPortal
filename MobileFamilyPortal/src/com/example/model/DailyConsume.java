package com.example.model;

import java.sql.Date;

public class DailyConsume {
	
	private int dailyID;
	private double amount;
	private Date date;
	
	private int getDailyID() {
		return dailyID;
	}
	private void setDailyID(int dailyID) {
		this.dailyID = dailyID;
	}
	private double getAmount() {
		return amount;
	}
	private void setAmount(double amount) {
		this.amount = amount;
	}
	private Date getDate() {
		return date;
	}
	private void setDate(Date date) {
		this.date = date;
	}
}
