package com.example.dal;

import java.util.ArrayList;
import java.util.List;

import com.example.base.BaseField;
import com.example.model.Consume;
import com.example.model.DailyConsume;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DailyConsumeDAL extends SQLiteOpenHelper {

	private SQLiteDatabase db;//数据库
	public DailyConsumeDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// 打开或新建数据库(第一次时创建)获得SQLiteDatabase对象，为了读取和写入数据
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	/*更新数据库*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(BaseField.DATABASE_TAG, "RECREATE TABLE "+BaseField.TABLE_NAME_DAILY_CONSUME);
		String sql = "DROP TABLE IF EXISTS " + BaseField.TABLE_NAME_DAILY_CONSUME;
		db.execSQL(sql);
		onCreate(db);
	}

	/*关闭数据库*/
	@Override
	public synchronized void close() {
		Log.i(BaseField.DATABASE_TAG, "CLOSE DATABASE");
		db.close();
		super.close();
	}

	/*打开数据库*/
	@Override
	public void onOpen(SQLiteDatabase db1) {
		Log.i(BaseField.DATABASE_TAG, "OPEN DATABASE");
		super.onOpen(db1);
	}
	/*添加日常消费Info和Details*/
	public boolean add(DailyConsume model){
    	long dailyID=addDailyConsume(model);
    	if(dailyID==-1){//添加日常消费Info失败
    		return false;
    	}
    	if(model.getConsumeList().size()>0){
    		for (Consume consume : model.getConsumeList()) {
				consume.setDailyID(Integer.parseInt(String.valueOf(dailyID)));
				long consumeID=addConsume(consume);
				if(consumeID==-1){//添加日常消费Detail失败
					return false;
				}
			}
		}
    	return true;
	}
	/*添加日常消费Info*/
	private long addDailyConsume(DailyConsume model) {
		/* 将新增的值放入ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("amount", model.getAmount());
		cv.put("date", model.getDate());
		long rowsaffected = db.insert(BaseField.TABLE_NAME_DAILY_CONSUME, null, cv);
		Log.i(BaseField.DATABASE_TAG, "ADD " + rowsaffected + " "+BaseField.TABLE_NAME_DAILY_CONSUME);
		return rowsaffected;
	}
	/*增加日常消费Detail*/
	private long addConsume(Consume model) {
		/* 将新增的值放入ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("description", model.getDescription());
		cv.put("amount", model.getAmount());
		cv.put("typeID", model.getTypeID());
		cv.put("dailyID", model.getDailyID());
		long rowsaffected = db.insert(BaseField.TABLE_NAME_CONSUME, null, cv);
		Log.i(BaseField.DATABASE_TAG, "ADD " + rowsaffected + " "+BaseField.TABLE_NAME_CONSUME);
		return rowsaffected;
	}
	/*删除日常消费Info&Details*/
	public boolean delete(int id){
		if(deleteDailyConsume(id)==0){//删除日常消费Info失败
			return false;
		}
		if(deleteConsumes(id)==0){//删除日常消费Details失败
			return false;
		}
		return true;
	}
	/*删除日常消费Info*/
	private int deleteDailyConsume(int id) {
		String[] whereArgs = { Integer.toString(id) };
		int rowsaffected = db.delete(BaseField.TABLE_NAME_DAILY_CONSUME, "dailyID=?", whereArgs);
		Log.i(BaseField.DATABASE_TAG, "DELETE " + rowsaffected + " "+BaseField.TABLE_NAME_DAILY_CONSUME);
		return rowsaffected;
	}
	/*删除日常消费Details*/
	private int deleteConsumes(int dailyID) {
		String[] whereArgs = { Integer.toString(dailyID) };
		int rowsaffected = db.delete(BaseField.TABLE_NAME_CONSUME, "dailyID=?", whereArgs);
		Log.i(BaseField.DATABASE_TAG, "DELETE " + rowsaffected + " "+BaseField.TABLE_NAME_CONSUME);
		return rowsaffected;
	}
	/* 修改日常消费Info
	 * 删除原日常消费Details
	 * 添加新日常消费Details
	 * */
	public boolean update(DailyConsume model){
		if(updateDailyConsume(model)==0){//更新日常消费Info失败
			return false;
		}
		if(deleteConsumes(model.getDailyID())==0){//删除日常消费Details失败
			return false;
		}
		if(model.getConsumeList().size()>0){
    		for (Consume consume : model.getConsumeList()) {
				consume.setDailyID(model.getDailyID());
				long consumeID=addConsume(consume);
				if(consumeID==-1){//添加日常消费Detail失败
					return false;
				}
			}
		}
		return true;
	}
	/*修改日常消费Info*/
	private int updateDailyConsume(DailyConsume model) {
		ContentValues cv = new ContentValues();
		cv.put("amount", model.getAmount());
		cv.put("date", model.getDate());
		String[] whereArgs = { Integer.toString(model.getDailyID()) };
		int rowsaffected = db.update(BaseField.TABLE_NAME_DAILY_CONSUME, cv, "dailyID=?",
				whereArgs);
		Log.i(BaseField.DATABASE_TAG, "UPDATE " +rowsaffected + " "+BaseField.TABLE_NAME_DAILY_CONSUME);
		return rowsaffected;
	}

	/*查询所有的数据 ，返回Cursor对象*/
	public Cursor query(String whereString) {
		Log.i(BaseField.DATABASE_TAG, "SELECT "+BaseField.TABLE_NAME_DAILY_CONSUME);
		/*结果集游标Cursor返回的数据中，一定要有一列名为“_id”*/
		String sqlString="SELECT dailyID as _id, amount, date FROM "+BaseField.TABLE_NAME_DAILY_CONSUME; 
		if(!whereString.equals("")){
			sqlString+=" "+whereString;
		}
		sqlString+=" ORDER BY date desc";
		return db.rawQuery(sqlString, null);
	}
	/*根据条件获取日常消费的Info和Details*/
	public DailyConsume queryModel(DailyConsume item, boolean isNeedDetails){
		DailyConsume model=queryDailyConsume(item);
		if(model==null){//获取日常消费Info失败
			return null;
		}
		if(isNeedDetails){//需要获取日常消费Details
			List<Consume> list=queryConsumes(model.getDailyID());
			model.setConsumeList(list);
			return model;
		}else{
			return model;
		}
	}
	/*根据条件获取日常消费的Info*/
	private DailyConsume queryDailyConsume(DailyConsume item){
		DailyConsume model=new DailyConsume();
		Log.i(BaseField.DATABASE_TAG, "SELECT One "+BaseField.TABLE_NAME_DAILY_CONSUME);
		String sql="SELECT dailyID as _id, amount, date FROM "+BaseField.TABLE_NAME_DAILY_CONSUME+" WHERE 1=1";
		if(item.getDailyID()!=0){
			sql+=" and _id="+item.getDailyID();
		}
		if(item.getDate()!=null){
			sql+=" and date='"+item.getDate()+"'";
		}
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext()){
				model.setDailyID(cursor.getInt(cursor.getColumnIndex("_id")));
				model.setAmount(cursor.getDouble(cursor.getColumnIndex("amount")));
				model.setDate(cursor.getString(cursor.getColumnIndex("date")));
			}
			return model;
		}else{
			return null;
		}
	}
	/*根据条件获取日常消费的Details*/
	private List<Consume> queryConsumes(int dailyID){
		List<Consume> list=new ArrayList<Consume>();
		Log.i(BaseField.DATABASE_TAG, "SELECT Multiple "+BaseField.TABLE_NAME_CONSUME);
		String sql="SELECT consumeID as _id, description, amount, typeID, dailyID FROM "+BaseField.TABLE_NAME_CONSUME+" WHERE 1=1";
		if(dailyID!=0){
			sql+=" and dailyID="+dailyID;
		}
		sql+=" ORDER BY typeID ASC";
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext()){
				Consume model=new Consume();
				model.setConsumeID(cursor.getInt(cursor.getColumnIndex("_id")));
				model.setDescription(cursor.getString(cursor.getColumnIndex("description")));
				model.setAmount(cursor.getDouble(cursor.getColumnIndex("amount")));
				model.setTypeID(cursor.getInt(cursor.getColumnIndex("typeID")));
				model.setDailyID(cursor.getInt(cursor.getColumnIndex("dailyID")));
				list.add(model);
			}
			return list;
		}else{
			return null;
		}
	}
}

