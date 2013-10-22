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

	private SQLiteDatabase db;//���ݿ�
	public DailyConsumeDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// �򿪻��½����ݿ�(��һ��ʱ����)���SQLiteDatabase����Ϊ�˶�ȡ��д������
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	/*�������ݿ�*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(BaseField.DATABASE_TAG, "RECREATE TABLE "+BaseField.TABLE_NAME_DAILY_CONSUME);
		String sql = "DROP TABLE IF EXISTS " + BaseField.TABLE_NAME_DAILY_CONSUME;
		db.execSQL(sql);
		onCreate(db);
	}

	/*�ر����ݿ�*/
	@Override
	public synchronized void close() {
		Log.i(BaseField.DATABASE_TAG, "CLOSE DATABASE");
		db.close();
		super.close();
	}

	/*�����ݿ�*/
	@Override
	public void onOpen(SQLiteDatabase db1) {
		Log.i(BaseField.DATABASE_TAG, "OPEN DATABASE");
		super.onOpen(db1);
	}
	/*����ճ�����Info��Details*/
	public boolean add(DailyConsume model){
    	long dailyID=addDailyConsume(model);
    	if(dailyID==-1){//����ճ�����Infoʧ��
    		return false;
    	}
    	if(model.getConsumeList().size()>0){
    		for (Consume consume : model.getConsumeList()) {
				consume.setDailyID(Integer.parseInt(String.valueOf(dailyID)));
				long consumeID=addConsume(consume);
				if(consumeID==-1){//����ճ�����Detailʧ��
					return false;
				}
			}
		}
    	return true;
	}
	/*����ճ�����Info*/
	private long addDailyConsume(DailyConsume model) {
		/* ��������ֵ����ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("amount", model.getAmount());
		cv.put("date", model.getDate());
		long rowsaffected = db.insert(BaseField.TABLE_NAME_DAILY_CONSUME, null, cv);
		Log.i(BaseField.DATABASE_TAG, "ADD " + rowsaffected + " "+BaseField.TABLE_NAME_DAILY_CONSUME);
		return rowsaffected;
	}
	/*�����ճ�����Detail*/
	private long addConsume(Consume model) {
		/* ��������ֵ����ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("description", model.getDescription());
		cv.put("amount", model.getAmount());
		cv.put("typeID", model.getTypeID());
		cv.put("dailyID", model.getDailyID());
		long rowsaffected = db.insert(BaseField.TABLE_NAME_CONSUME, null, cv);
		Log.i(BaseField.DATABASE_TAG, "ADD " + rowsaffected + " "+BaseField.TABLE_NAME_CONSUME);
		return rowsaffected;
	}
	/*ɾ���ճ�����Info&Details*/
	public boolean delete(int id){
		if(deleteDailyConsume(id)==0){//ɾ���ճ�����Infoʧ��
			return false;
		}
		if(deleteConsumes(id)==0){//ɾ���ճ�����Detailsʧ��
			return false;
		}
		return true;
	}
	/*ɾ���ճ�����Info*/
	private int deleteDailyConsume(int id) {
		String[] whereArgs = { Integer.toString(id) };
		int rowsaffected = db.delete(BaseField.TABLE_NAME_DAILY_CONSUME, "dailyID=?", whereArgs);
		Log.i(BaseField.DATABASE_TAG, "DELETE " + rowsaffected + " "+BaseField.TABLE_NAME_DAILY_CONSUME);
		return rowsaffected;
	}
	/*ɾ���ճ�����Details*/
	private int deleteConsumes(int dailyID) {
		String[] whereArgs = { Integer.toString(dailyID) };
		int rowsaffected = db.delete(BaseField.TABLE_NAME_CONSUME, "dailyID=?", whereArgs);
		Log.i(BaseField.DATABASE_TAG, "DELETE " + rowsaffected + " "+BaseField.TABLE_NAME_CONSUME);
		return rowsaffected;
	}
	/* �޸��ճ�����Info
	 * ɾ��ԭ�ճ�����Details
	 * ������ճ�����Details
	 * */
	public boolean update(DailyConsume model){
		if(updateDailyConsume(model)==0){//�����ճ�����Infoʧ��
			return false;
		}
		if(deleteConsumes(model.getDailyID())==0){//ɾ���ճ�����Detailsʧ��
			return false;
		}
		if(model.getConsumeList().size()>0){
    		for (Consume consume : model.getConsumeList()) {
				consume.setDailyID(model.getDailyID());
				long consumeID=addConsume(consume);
				if(consumeID==-1){//����ճ�����Detailʧ��
					return false;
				}
			}
		}
		return true;
	}
	/*�޸��ճ�����Info*/
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

	/*��ѯ���е����� ������Cursor����*/
	public Cursor query(String whereString) {
		Log.i(BaseField.DATABASE_TAG, "SELECT "+BaseField.TABLE_NAME_DAILY_CONSUME);
		/*������α�Cursor���ص������У�һ��Ҫ��һ����Ϊ��_id��*/
		String sqlString="SELECT dailyID as _id, amount, date FROM "+BaseField.TABLE_NAME_DAILY_CONSUME; 
		if(!whereString.equals("")){
			sqlString+=" "+whereString;
		}
		sqlString+=" ORDER BY date desc";
		return db.rawQuery(sqlString, null);
	}
	/*����������ȡ�ճ����ѵ�Info��Details*/
	public DailyConsume queryModel(DailyConsume item, boolean isNeedDetails){
		DailyConsume model=queryDailyConsume(item);
		if(model==null){//��ȡ�ճ�����Infoʧ��
			return null;
		}
		if(isNeedDetails){//��Ҫ��ȡ�ճ�����Details
			List<Consume> list=queryConsumes(model.getDailyID());
			model.setConsumeList(list);
			return model;
		}else{
			return model;
		}
	}
	/*����������ȡ�ճ����ѵ�Info*/
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
	/*����������ȡ�ճ����ѵ�Details*/
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

