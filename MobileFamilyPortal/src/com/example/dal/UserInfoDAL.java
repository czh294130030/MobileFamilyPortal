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

	private SQLiteDatabase db;//���ݿ�
	public UserInfoDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// �򿪻��½����ݿ�(��һ��ʱ����)���SQLiteDatabase����Ϊ�˶�ȡ��д������
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	/*�������ݿ�*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(BaseField.DATABASE_TAG, "RECREATE TABLE "+BaseField.TABLE_NAME_USERINFO);
		String sql = "DROP TABLE IF EXISTS " + BaseField.TABLE_NAME_USERINFO;
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
	public long add(UserInfo model) {
		/* ��������ֵ����ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("account", model.getAccount());
		cv.put("userName", model.getUserName());
		cv.put("password", model.getPassword());
		long rowsaffected = db.insert(BaseField.TABLE_NAME_USERINFO, null, cv);
		Log.i(BaseField.DATABASE_TAG, "ADD " + rowsaffected + " "+BaseField.TABLE_NAME_USERINFO);
		return rowsaffected;
	}

	/*ɾ��*/
	public int delete(int id) {
		String[] whereArgs = { Integer.toString(id) };
		int rowsaffected = db.delete(BaseField.TABLE_NAME_USERINFO, "userID=?", whereArgs);
		Log.i(BaseField.DATABASE_TAG, "DELETE " + rowsaffected + " "+BaseField.TABLE_NAME_USERINFO);
		return rowsaffected;
	}

	/*�޸�*/
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

	/*��ѯ���е����� ������Cursor����*/
	public Cursor query(String whereString) {
		Log.i(BaseField.DATABASE_TAG, "SELECT "+BaseField.TABLE_NAME_USERINFO);
		/*������α�Cursor���ص������У�һ��Ҫ��һ����Ϊ��_id��*/
		String sqlString="SELECT userID as _id, account, userName, password FROM "+BaseField.TABLE_NAME_USERINFO; 
		if(!whereString.equals("")){
			sqlString+=" "+whereString;
		}
		return db.rawQuery(sqlString, null);
	}
	
	/*����������ȡModel*/
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

