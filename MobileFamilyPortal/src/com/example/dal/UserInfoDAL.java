package com.example.dal;

import com.example.base.BaseField;
import com.example.mobilefamilyportal.R.string;
import com.example.model.UserInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserInfoDAL extends SQLiteOpenHelper {

	private SQLiteDatabase db;//���ݿ�
	private static final String TABLE_NAME = "UserInfo";//���� 
	private static final String TAG = "UserInfo";
	public UserInfoDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// �򿪻��½����ݿ�(��һ��ʱ����)���SQLiteDatabase����Ϊ�˶�ȡ��д������
	}

	/*������*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "CREATE TABLE UserInfo");
		String sql = "CREATE TABLE "+TABLE_NAME
				+ "(userID INTEGER PRIMARY KEY AUTOINCREMENT,account NVARCHAR(50),userName NVARCHAR(50),password NVARCHAR(50))";
		db.execSQL(sql);
	}

	/*�������ݿ�*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "RECREATE TABLE UserInfo");
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	/*�ر����ݿ�*/
	@Override
	public synchronized void close() {
		Log.i(TAG, "CLOSE DATABASE");
		db.close();
		super.close();
	}

	/*�����ݿ�*/
	@Override
	public void onOpen(SQLiteDatabase db1) {
		Log.i(TAG, "OPEN DATABASE");
		super.onOpen(db1);
	}

	/*����*/
	public long add(UserInfo model) {
		/* ��������ֵ����ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("account", model.getAccount());
		cv.put("userName", model.getUserName());
		cv.put("password", model.getPassword());
		long rowsaffected = db.insert(TABLE_NAME, null, cv);
		Log.i(TAG, "ADD " + rowsaffected + " UserInfo");
		return rowsaffected;
	}

	/*ɾ��*/
	public int delete(int id) {
		String[] whereArgs = { Integer.toString(id) };
		int rowsaffected = db.delete(TABLE_NAME, "userID=?", whereArgs);
		Log.i(TAG, "DELETE " + rowsaffected + " UserInfo");
		return rowsaffected;
	}

	/*�޸�*/
	public int update(UserInfo model) {
		ContentValues cv = new ContentValues();
		cv.put("account", model.getAccount());
		cv.put("userName", model.getUserName());
		cv.put("password", model.getPassword());
		String[] whereArgs = { Integer.toString(model.getUserID()) };
		int rowsaffected = db.update(TABLE_NAME, cv, "userID=?",
				whereArgs);
		Log.i(TAG, "UPDATE " +rowsaffected + " UserInfo");
		return rowsaffected;
	}

	/*��ѯ���е����� ������Cursor����*/
	public Cursor query(String whereString) {
		Log.i(TAG, "SELECT UserInfo");
		/*������α�Cursor���ص������У�һ��Ҫ��һ����Ϊ��_id��*/
		String sqlString="SELECT userID as _id, account, userName, password FROM "+TABLE_NAME; 
		if(!whereString.equals("")){
			sqlString+=" "+whereString;
		}
		return db.rawQuery(sqlString, null);
	}
	
	/*����id��ȡ�û���Ϣ*/
	public UserInfo queryUserInfoByID(int id){
		UserInfo model=new UserInfo();
		Log.i(TAG, "SELECT One UserInfo");
		Cursor cursor=db.rawQuery("SELECT userID as _id, account, userName, password FROM "+TABLE_NAME+" WHERE userID="+id, null);
		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext()){
				model.setUserID(cursor.getInt(cursor.getColumnIndex("_id")));
				model.setAccount(cursor.getString(cursor.getColumnIndex("account")));
				model.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
				model.setPassword(cursor.getString(cursor.getColumnIndex("password")));
			}
			return model;
		}else{
			return null;
		}
	}
}

