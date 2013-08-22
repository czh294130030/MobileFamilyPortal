package com.example.dal;

import com.example.base.BaseField;
import com.example.model.UserInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserInfoDAL extends SQLiteOpenHelper {

	private SQLiteDatabase db;//数据库
	private static final String TABLE_NAME = "UserInfo";//表名 
	private static final String TAG = "UserInfo";
	public UserInfoDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// 打开或新建数据库(第一次时创建)获得SQLiteDatabase对象，为了读取和写入数据
	}

	/*创建表*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "CREATE TABLE UserInfo");
		String sql = "CREATE TABLE "+TABLE_NAME
				+ "(userID INTEGER PRIMARY KEY AUTOINCREMENT,account NVARCHAR(50),userName NVARCHAR(50),password NVARCHAR(50))";
		db.execSQL(sql);
	}

	/*更新数据库*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "RECREATE TABLE UserInfo");
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	/*关闭数据库*/
	@Override
	public synchronized void close() {
		Log.i(TAG, "CLOSE DATABASE");
		db.close();
		super.close();
	}

	/*打开数据库*/
	@Override
	public void onOpen(SQLiteDatabase db1) {
		Log.i(TAG, "OPEN DATABASE");
		super.onOpen(db1);
	}

	/*增加*/
	public long add(UserInfo model) {
		/* 将新增的值放入ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("account", model.getAccount());
		cv.put("userName", model.getUserName());
		cv.put("password", model.getPassword());
		long rowsaffected = db.insert(TABLE_NAME, null, cv);
		Log.i(TAG, "ADD" + rowsaffected + "UserInfo");
		return rowsaffected;
	}

	/*删除*/
	public int delete(int id) {
		String[] whereArgs = { Integer.toString(id) };
		int rowsaffected = db.delete(TABLE_NAME, "userID=?", whereArgs);
		Log.i(TAG, "DELETE" + rowsaffected + "UserInfo");
		return rowsaffected;
	}

	/*修改*/
	public int update(UserInfo model) {
		ContentValues cv = new ContentValues();
		cv.put("account", model.getAccount());
		cv.put("userName", model.getUserName());
		cv.put("password", model.getPassword());
		String[] whereArgs = { Integer.toString(model.getUserID()) };
		int rowsaffected = db.update(TABLE_NAME, cv, "userID=?",
				whereArgs);
		Log.i(TAG, "UPDATE" +rowsaffected + "UserInfo");
		return rowsaffected;
	}

	/*查询所有的数据 ，返回Cursor对象*/
	public Cursor query() {
		try {
			//asc是升序desc为降序（默认为asc）
			return db.query(TABLE_NAME, null, null, null, null, null, "userID ASC");
		} catch (Exception e) {
			return null;
		}
	}

}

