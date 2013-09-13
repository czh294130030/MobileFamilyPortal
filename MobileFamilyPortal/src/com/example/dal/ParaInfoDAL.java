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

	private SQLiteDatabase db;//���ݿ�
	public ParaInfoDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// �򿪻��½����ݿ�(��һ��ʱ����)���SQLiteDatabase����Ϊ�˶�ȡ��д������
	}

	/*������*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		createParaInfo(db);
		createParaDetail(db);
		createUserInfo(db);
		createBankCard(db);
	}
	/*�������п���Ϣ��*/
	private void createBankCard(SQLiteDatabase db){
		Log.i(BaseField.DATABASE_TAG, "CREATE TABLE "+BaseField.TABLE_NAME_BANK_CARD);
		String sql="CREATE TABLE "+BaseField.TABLE_NAME_BANK_CARD
				+"(cardID INTEGER PRIMARY KEY AUTOINCREMENT,cardNO NVARCHAR(50),userID INTEGER,bankID INTEGER,cityID INTEGER);";
		db.execSQL(sql);
	}
	/*����������Ϣ����������*/
	private void createParaInfo(SQLiteDatabase db){
		Log.i(BaseField.DATABASE_TAG, "CREATE TABLE "+BaseField.TABLE_NAME_PARAINFO);
		String sql = "CREATE TABLE "+BaseField.TABLE_NAME_PARAINFO
				+ "(infoID INTEGER PRIMARY KEY AUTOINCREMENT,description NVARCHAR(50));";
		db.execSQL(sql);
		/*��������*/
		List<ParaInfo> items=BaseMethod.getParaInfos();
		if(items.size()>0){
			for (ParaInfo item : items) {
				String sqlInsert="INSERT INTO "+BaseField.TABLE_NAME_PARAINFO+" VALUES(null,'"+item.getDescription()+"');";
				db.execSQL(sqlInsert);
			}
		}
	}
	/*����������ϸ����������*/
	private void createParaDetail(SQLiteDatabase db){
		Log.i(BaseField.DATABASE_TAG, "CREATE TABLE "+BaseField.TABLE_NAME_PARADETAIL);
		String sql = "CREATE TABLE "+BaseField.TABLE_NAME_PARADETAIL
				+ "(detailID INTEGER PRIMARY KEY AUTOINCREMENT,description NVARCHAR(50),infoID INTEGER);";
		db.execSQL(sql);
		/*��������*/
		List<ParaDetail> items=BaseMethod.getParaDetails();
		if(items.size()>0){
			for (ParaDetail item : items) {
				String sqlInsert="INSERT INTO "+BaseField.TABLE_NAME_PARADETAIL+" VALUES(null,'"+item.getDescription()+"',"+item.getInfoID()+");";
				db.execSQL(sqlInsert);
			}
		}
	}
	/*�����û���Ϣ����������*/
	private void createUserInfo(SQLiteDatabase db){
		Log.i(BaseField.DATABASE_TAG, "CREATE TABLE "+BaseField.TABLE_NAME_USERINFO);
		String sql = "CREATE TABLE "+BaseField.TABLE_NAME_USERINFO
				+ "(userID INTEGER PRIMARY KEY AUTOINCREMENT,account NVARCHAR(50),userName NVARCHAR(50),password NVARCHAR(50));";
		db.execSQL(sql);
		/*��������*/
		UserInfo itemInfo=BaseMethod.getAdminInfo();
		String sqlInsert="INSERT INTO "+BaseField.TABLE_NAME_USERINFO+" VALUES(null,'"+itemInfo.getAccount()+"','"+itemInfo.getUserName()+"','"+itemInfo.getPassword()+"');";
		db.execSQL(sqlInsert);
	}

	/*�������ݿ�*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(BaseField.DATABASE_TAG, "RECREATE TABLE "+BaseField.TABLE_NAME_PARAINFO);
		String sql = "DROP TABLE IF EXISTS " + BaseField.TABLE_NAME_PARAINFO;
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
	
	/*��ѯ���е����� ������Cursor����*/
	public Cursor query(String whereString) {
		Log.i(BaseField.DATABASE_TAG, "SELECT "+BaseField.TABLE_NAME_PARAINFO);
		/*������α�Cursor���ص������У�һ��Ҫ��һ����Ϊ��_id��*/
		String sqlString="SELECT infoID as _id, description FROM "+BaseField.TABLE_NAME_PARAINFO; 
		if(!whereString.equals("")){
			sqlString+=" "+whereString;
		}
		return db.rawQuery(sqlString, null);
	}
}

