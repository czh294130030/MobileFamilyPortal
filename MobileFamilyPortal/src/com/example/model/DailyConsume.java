package com.example.model;

import java.util.List;

public class DailyConsume {
	
	private int dailyID;
	private double amount;
	private String date;
	private List<Consume> consumeList;
	
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<Consume> getConsumeList() {
		return consumeList;
	}
	public void setConsumeList(List<Consume> consumeList) {
		this.consumeList = consumeList;
	}
}
