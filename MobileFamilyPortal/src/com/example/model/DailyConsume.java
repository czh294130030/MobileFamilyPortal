package com.example.model;

import java.sql.Date;

public class DailyConsume {
	
	private int dailyID;
	private double amount;
	private Date date;
	
	public int getDailyID() {
		return dailyID;
	}
	public void setDailyID(int dailyID) {
		this.dailyID = dailyID;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
