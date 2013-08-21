package com.example.model;

public class Consume {

	private int consumeID;
	private String description;
	private double amount;
	private int typeID;
	private int dailyID;
	
	private int getConsumeID() {
		return consumeID;
	}
	private void setConsumeID(int consumeID) {
		this.consumeID = consumeID;
	}
	private String getDescription() {
		return description;
	}
	private void setDescription(String description) {
		this.description = description;
	}
	private double getAmount() {
		return amount;
	}
	private void setAmount(double amount) {
		this.amount = amount;
	}
	private int getTypeID() {
		return typeID;
	}
	private void setTypeID(int typeID) {
		this.typeID = typeID;
	}
	private int getDailyID() {
		return dailyID;
	}
	private void setDailyID(int dailyID) {
		this.dailyID = dailyID;
	}
}
