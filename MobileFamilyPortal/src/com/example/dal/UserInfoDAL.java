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
	public UserInfoDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// 打开或新建数据库(第一次时创建)获得SQLiteDatabase对象，为了读取和写入数据
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	/*更新数据库*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(BaseField.DATABASE_TAG, "RECREATE TABLE "+BaseField.TABLE_NAME_USERINFO);
		String sql = "DROP TABLE IF EXISTS " + BaseField.TABLE_NAME_USERINFO;
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

	/*增加*/
	public long add(UserInfo model) {
		/* 将新增的值放入ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("account", model.getAccount());
		cv.put("userName", model.getUserName());
		cv.put("password", model.getPassword());
		long rowsaffected = db.insert(BaseField.TABLE_NAME_USERINFO, null, cv);
		Log.i(BaseField.DATABASE_TAG, "ADD " + rowsaffected + " "+BaseField.TABLE_NAME_USERINFO);
		return rowsaffected;
	}

	/*删除*/
	public int delete(int id) {
		String[] whereArgs = { Integer.toString(id) };
		int rowsaffected = db.delete(BaseField.TABLE_NAME_USERINFO, "userID=?", whereArgs);
		Log.i(BaseField.DATABASE_TAG, "DELETE " + rowsaffected + " "+BaseField.TABLE_NAME_USERINFO);
		return rowsaffected;
	}

	/*修改*/
	public int update(UserInfo model) {
		ContentValues cv = new ContentValues();
		cv.put("account", model.getAccount());
		cv.put("userName", model.getUserName());
		cv.put("password", model.getPassword());
		String[] whereArgs = { Integer.toString(model.getUserID()) };
		int rowsaffected = db.update(BaseField.TABLE_NAME_USERINFO, cv, "userID=?",
				whereArgs);
		Log.i(BaseField.DATABASE_TAG, "UPDATE " +rowsaffected + " "+BaseField.TABLE_NAME_USERINFO);
		return rowsaffected;
	}

	/*查询所有的数据 ，返回Cursor对象*/
	public Cursor query(String whereString) {
		Log.i(BaseField.DATABASE_TAG, "SELECT "+BaseField.TABLE_NAME_USERINFO);
		/*结果集游标Cursor返回的数据中，一定要有一列名为“_id”*/
		String sqlString="SELECT userID as _id, account, userName, password FROM "+BaseField.TABLE_NAME_USERINFO; 
		if(!whereString.equals("")){
			sqlString+=" "+whereString;
		}
		return db.rawQuery(sqlString, null);
	}
	
	/*根据条件获取Model*/
	public UserInfo queryModel(UserInfo item){
		UserInfo model=new UserInfo();
		Log.i(BaseField.DATABASE_TAG, "SELECT One "+BaseField.TABLE_NAME_USERINFO);
		String sql="SELECT userID as _id, account, userName, password FROM "+BaseField.TABLE_NAME_USERINFO+" WHERE 1=1";
		if(item.getUserID()!=0){
			sql+=" and _id="+item.getUserID();
		}
		if(item.getAccount()!=null){
			sql+=" and account='"+item.getAccount()+"'";
		}
		if(item.getUserName()!=null){
			sql+=" and userName='"+item.getUserName()+"'";
		}
		if(item.getPassword()!=null){
			sql+=" and password='"+item.getPassword()+"'";
		}
		Cursor cursor=db.rawQuery(sql, null);
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

