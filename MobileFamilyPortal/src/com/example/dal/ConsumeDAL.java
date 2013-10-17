package com.example.dal;

import java.util.ArrayList;
import java.util.List;

import com.example.base.BaseField;
import com.example.model.Consume;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConsumeDAL extends SQLiteOpenHelper {

	private SQLiteDatabase db;//���ݿ�
	public ConsumeDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// �򿪻��½����ݿ�(��һ��ʱ����)���SQLiteDatabase����Ϊ�˶�ȡ��д������
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	/*�������ݿ�*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(BaseField.DATABASE_TAG, "RECREATE TABLE "+BaseField.TABLE_NAME_CONSUME);
		String sql = "DROP TABLE IF EXISTS " + BaseField.TABLE_NAME_CONSUME;
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

	/*����*/
	public long add(Consume model) {
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

	/*ɾ��*/
	public int delete(int id) {
		String[] whereArgs = { Integer.toString(id) };
		int rowsaffected = db.delete(BaseField.TABLE_NAME_CONSUME, "consumeID=?", whereArgs);
		Log.i(BaseField.DATABASE_TAG, "DELETE " + rowsaffected + " "+BaseField.TABLE_NAME_CONSUME);
		return rowsaffected;
	}

	/*�޸�*/
	public int update(Consume model) {
		ContentValues cv = new ContentValues();
		cv.put("description", model.getDescription());
		cv.put("amount", model.getAmount());
		cv.put("typeID", model.getTypeID());
		cv.put("dailyID", model.getDailyID());
		String[] whereArgs = { Integer.toString(model.getConsumeID()) };
		int rowsaffected = db.update(BaseField.TABLE_NAME_CONSUME, cv, "consumeID=?",
				whereArgs);
		Log.i(BaseField.DATABASE_TAG, "UPDATE " +rowsaffected + " "+BaseField.TABLE_NAME_CONSUME);
		return rowsaffected;
	}

	/*��ѯ���е����� ������Cursor����*/
	public Cursor query(String whereString) {
		Log.i(BaseField.DATABASE_TAG, "SELECT "+BaseField.TABLE_NAME_CONSUME);
		/*������α�Cursor���ص������У�һ��Ҫ��һ����Ϊ��_id��*/
		String sqlString="SELECT consumeID as _id, description, amount, typeID, dailyID FROM "+BaseField.TABLE_NAME_CONSUME; 
		if(!whereString.equals("")){
			sqlString+=" "+whereString;
		}
		return db.rawQuery(sqlString, null);
	}
	
	/*����������ȡModel*/
	public Consume queryModel(Consume item){
		Consume model=new Consume();
		Log.i(BaseField.DATABASE_TAG, "SELECT One "+BaseField.TABLE_NAME_CONSUME);
		String sql="SELECT consumeID as _id, description, amount, typeID, dailyID FROM "+BaseField.TABLE_NAME_CONSUME+" WHERE 1=1";
		if(item.getConsumeID()!=0){
			sql+=" and _id="+item.getConsumeID();
		}
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext()){
				model.setConsumeID(cursor.getInt(cursor.getColumnIndex("_id")));
				model.setDescription(cursor.getString(cursor.getColumnIndex("description")));
				model.setAmount(cursor.getDouble(cursor.getColumnIndex("amount")));
				model.setTypeID(cursor.getInt(cursor.getColumnIndex("typeID")));
				model.setDailyID(cursor.getInt(cursor.getColumnIndex("dailyID")));
			}
			return model;
		}else{
			return null;
		}
	}
	/*����dailyID��ȡConsume*/
	public List<Consume> queryList(int dailyID){
		List<Consume> list=new ArrayList<Consume>();
		Log.i(BaseField.DATABASE_TAG, "SELECT Multiple "+BaseField.TABLE_NAME_CONSUME);
		String sql="SELECT consumeID as _id, description, amount, typeID, dailyID FROM "+BaseField.TABLE_NAME_CONSUME+" WHERE 1=1";
		if(dailyID!=0){
			sql+=" and dailyID="+dailyID;
		}
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

