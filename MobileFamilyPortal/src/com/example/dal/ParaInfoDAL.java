package com.example.dal;

import java.util.List;
import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.model.ParaDetail;
import com.example.model.ParaInfo;
import com.example.model.UserInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ParaInfoDAL extends SQLiteOpenHelper {

	private SQLiteDatabase db;//数据库
	public ParaInfoDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// 打开或新建数据库(第一次时创建)获得SQLiteDatabase对象，为了读取和写入数据
	}

	/*创建表*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		createParaInfo(db);
		createParaDetail(db);
		createUserInfo(db);
		createBankCard(db);
	}
	/*创建银行卡信息表*/
	private void createBankCard(SQLiteDatabase db){
		Log.i(BaseField.DATABASE_TAG, "CREATE TABLE "+BaseField.TABLE_NAME_BANK_CARD);
		String sql="CREATE TABLE "+BaseField.TABLE_NAME_BANK_CARD
				+"(cardID INTEGER PRIMARY KEY AUTOINCREMENT,cardNO NVARCHAR(50),userID INTEGER,bankID INTEGER,cityID INTEGER);";
		db.execSQL(sql);
	}
	/*创建参数信息表并内置数据*/
	private void createParaInfo(SQLiteDatabase db){
		Log.i(BaseField.DATABASE_TAG, "CREATE TABLE "+BaseField.TABLE_NAME_PARAINFO);
		String sql = "CREATE TABLE "+BaseField.TABLE_NAME_PARAINFO
				+ "(infoID INTEGER PRIMARY KEY AUTOINCREMENT,description NVARCHAR(50));";
		db.execSQL(sql);
		/*内置数据*/
		List<ParaInfo> items=BaseMethod.getParaInfos();
		if(items.size()>0){
			for (ParaInfo item : items) {
				String sqlInsert="INSERT INTO "+BaseField.TABLE_NAME_PARAINFO+" VALUES(null,'"+item.getDescription()+"');";
				db.execSQL(sqlInsert);
			}
		}
	}
	/*创建参数明细表并内置数据*/
	private void createParaDetail(SQLiteDatabase db){
		Log.i(BaseField.DATABASE_TAG, "CREATE TABLE "+BaseField.TABLE_NAME_PARADETAIL);
		String sql = "CREATE TABLE "+BaseField.TABLE_NAME_PARADETAIL
				+ "(detailID INTEGER PRIMARY KEY AUTOINCREMENT,description NVARCHAR(50),infoID INTEGER);";
		db.execSQL(sql);
		/*内置数据*/
		List<ParaDetail> items=BaseMethod.getParaDetails();
		if(items.size()>0){
			for (ParaDetail item : items) {
				String sqlInsert="INSERT INTO "+BaseField.TABLE_NAME_PARADETAIL+" VALUES(null,'"+item.getDescription()+"',"+item.getInfoID()+");";
				db.execSQL(sqlInsert);
			}
		}
	}
	/*创建用户信息表并内置数据*/
	private void createUserInfo(SQLiteDatabase db){
		Log.i(BaseField.DATABASE_TAG, "CREATE TABLE "+BaseField.TABLE_NAME_USERINFO);
		String sql = "CREATE TABLE "+BaseField.TABLE_NAME_USERINFO
				+ "(userID INTEGER PRIMARY KEY AUTOINCREMENT,account NVARCHAR(50),userName NVARCHAR(50),password NVARCHAR(50));";
		db.execSQL(sql);
		/*内置数据*/
		UserInfo itemInfo=BaseMethod.getAdminInfo();
		String sqlInsert="INSERT INTO "+BaseField.TABLE_NAME_USERINFO+" VALUES(null,'"+itemInfo.getAccount()+"','"+itemInfo.getUserName()+"','"+itemInfo.getPassword()+"');";
		db.execSQL(sqlInsert);
	}

	/*更新数据库*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(BaseField.DATABASE_TAG, "RECREATE TABLE "+BaseField.TABLE_NAME_PARAINFO);
		String sql = "DROP TABLE IF EXISTS " + BaseField.TABLE_NAME_PARAINFO;
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
	
	/*查询所有的数据 ，返回Cursor对象*/
	public Cursor query(String whereString) {
		Log.i(BaseField.DATABASE_TAG, "SELECT "+BaseField.TABLE_NAME_PARAINFO);
		/*结果集游标Cursor返回的数据中，一定要有一列名为“_id”*/
		String sqlString="SELECT infoID as _id, description FROM "+BaseField.TABLE_NAME_PARAINFO; 
		if(!whereString.equals("")){
			sqlString+=" "+whereString;
		}
		return db.rawQuery(sqlString, null);
	}
}

