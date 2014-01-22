package com.example.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class DailyConsume {
	
	private int dailyID;
	private double amount;
	private String date;
	private List<Consume> consumeList;
	
	/*将DailyConsume转化为Json方便在网络中传输*/
	public static String ConvertToJson(List<DailyConsume> items){
		String jsonString=""; 
		if(items.size()>0){
			try {
				JSONArray jsonArray=new JSONArray();
				for (DailyConsume item : items) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("dailyID", item.getDailyID());
					jsonObject.put("amount", item.getAmount());
					jsonObject.put("date", item.getDate());
					JSONArray consumeJsonArray=Consume.ConvertToJson(item.getConsumeList());
					if(consumeJsonArray.length()>0){
						jsonObject.put("consumeList", consumeJsonArray);
					}
					jsonArray.put(jsonObject);
				}
				jsonString=jsonArray.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonString;
	}
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
