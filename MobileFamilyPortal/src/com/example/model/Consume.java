package com.example.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Consume {

	private int consumeID;
	private String description;
	private double amount;
	private int typeID;
	private int dailyID;
	
	/*将Consume转化为Json方便在网络中传输*/
	public static JSONArray ConvertToJson(List<Consume> items){
		JSONArray jsonArray=new JSONArray();
		if(items.size()>0){
			try {
				for (Consume item : items) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("consumeID", item.getConsumeID());
					jsonObject.put("description", item.getDescription());
					jsonObject.put("amount", item.getAmount());
					jsonObject.put("typeID", item.getTypeID());
					jsonObject.put("dailyID", item.getDailyID());
					jsonArray.put(jsonObject);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}
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
