package com.example.dal;

import com.example.base.BaseField;
import com.example.model.BankCard;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BankCardDAL extends SQLiteOpenHelper {

	private SQLiteDatabase db;//���ݿ�
	public BankCardDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// �򿪻��½����ݿ�(��һ��ʱ����)���SQLiteDatabase����Ϊ�˶�ȡ��д������
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	/*�������ݿ�*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(BaseField.DATABASE_TAG, "RECREATE TABLE "+BaseField.TABLE_NAME_BANK_CARD);
		String sql = "DROP TABLE IF EXISTS " + BaseField.TABLE_NAME_BANK_CARD;
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
	public long add(BankCard model) {
		/* ��������ֵ����ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("cardNO", model.getCardNO());
		cv.put("userID", model.getUserID());
		cv.put("bankID", model.getBankID());
		cv.put("cityID", model.getCityID());
		long rowsaffected = db.insert(BaseField.TABLE_NAME_BANK_CARD, null, cv);
		Log.i(BaseField.DATABASE_TAG, "ADD " + rowsaffected + " "+BaseField.TABLE_NAME_BANK_CARD);
		return rowsaffected;
	}

	/*ɾ��*/
	public int delete(int id) {
		String[] whereArgs = { Integer.toString(id) };
		int rowsaffected = db.delete(BaseField.TABLE_NAME_BANK_CARD, "cardID=?", whereArgs);
		Log.i(BaseField.DATABASE_TAG, "DELETE " + rowsaffected + " "+BaseField.TABLE_NAME_BANK_CARD);
		return rowsaffected;
	}

	/*�޸�*/
	public int update(BankCard model) {
		ContentValues cv = new ContentValues();
		cv.put("cardNO", model.getCardNO());
		cv.put("userID", model.getUserID());
		cv.put("bankID", model.getBankID());
		cv.put("cityID", model.getCityID());
		String[] whereArgs = { Integer.toString(model.getCardID()) };
		int rowsaffected = db.update(BaseField.TABLE_NAME_BANK_CARD, cv, "cardID=?",
				whereArgs);
		Log.i(BaseField.DATABASE_TAG, "UPDATE " +rowsaffected + " "+BaseField.TABLE_NAME_BANK_CARD);
		return rowsaffected;
	}

	/*��ѯ������ ������Cursor����*/
	public Cursor query(String whereString) {
		Log.i(BaseField.DATABASE_TAG, "SELECT "+BaseField.TABLE_NAME_BANK_CARD);
		/*������α�Cursor���ص������У�һ��Ҫ��һ����Ϊ��_id��*/
		String sqlString="SELECT " +
								"a.cardID as _id, " +
								"a.cardNO, " +
								"a.userID, " +
								"a.bankID, " +
								"a.cityID, " +
								"b.userName, "+
								"c.description as cardType, "+
								"d.description as city "+
								 " FROM "+BaseField.TABLE_NAME_BANK_CARD +" AS a"+
								 " INNER JOIN "+BaseField.TABLE_NAME_USERINFO+" AS b"+
								 " ON a.userID=b.userID"+
								 " INNER JOIN "+BaseField.TABLE_NAME_PARADETAIL+" AS c"+
								 " ON a.bankID=c.detailID"+
								 " INNER JOIN "+BaseField.TABLE_NAME_PARADETAIL+" AS d"+
								 " ON a.cityID=d.detailID"; 
		if(!whereString.equals("")){
			sqlString+=" "+whereString;
		}
		return db.rawQuery(sqlString, null);
	}	
	/*����������ȡModel*/
	public BankCard queryModel(BankCard item){
		BankCard model=new BankCard();
		Log.i(BaseField.DATABASE_TAG, "SELECT One "+BaseField.TABLE_NAME_BANK_CARD);
		String sql="SELECT cardID as _id, cardNO, userID, bankID, cityID FROM "+BaseField.TABLE_NAME_BANK_CARD+" WHERE 1=1";
		if(item.getCardID()!=0){
			sql+=" and _id="+item.getCardID();
		}
		if(item.getCardNO()!=null){
			sql+=" and cardNO='"+item.getCardNO()+"'";
		}
		if(item.getUserID()!=0){
			sql+=" and userID="+item.getUserID();
		}
		if(item.getBankID()!=0){
			sql+=" and bankID="+item.getBankID();
		}
		if(item.getCityID()!=0){
			sql+=" and cityID="+item.getCityID();
		}
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext()){
				model.setCardID(cursor.getInt(cursor.getColumnIndex("_id")));
				model.setCardNO(cursor.getString(cursor.getColumnIndex("cardNO")));
				model.setUserID(cursor.getInt(cursor.getColumnIndex("userID")));
				model.setBankID(cursor.getInt(cursor.getColumnIndex("bankID")));
				model.setCityID(cursor.getInt(cursor.getColumnIndex("cityID")));
			}
			return model;
		}else{
			return null;
		}
	}
}

