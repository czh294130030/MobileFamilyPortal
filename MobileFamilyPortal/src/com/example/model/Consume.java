package com.example.model;

public class Consume {

	private int consumeID;
	private String description;
	private double amount;
	private int typeID;
	private int dailyID;
	
	public int getConsumeID() {
		return consumeID;
	}
	public void setConsumeID(int consumeID) {
		this.consumeID = consumeID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getTypeID() {
		return typeID;
	}
	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}
	public int getDailyID() {
		return dailyID;
	}
	public void setDailyID(int dailyID) {
		this.dailyID = dailyID;
	}
}
