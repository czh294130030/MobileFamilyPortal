package com.example.dal;

import java.util.ArrayList;
import java.util.List;
import com.example.base.BaseField;
import com.example.model.KeyValue;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ParaDetailDAL extends SQLiteOpenHelper {

	private SQLiteDatabase db;//���ݿ�
	public ParaDetailDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// �򿪻��½����ݿ�(��һ��ʱ����)���SQLiteDatabase����Ϊ�˶�ȡ��д������
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	/*�������ݿ�*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(BaseField.DATABASE_TAG, "RECREATE TABLE "+BaseField.TABLE_NAME_PARADETAIL);
		String sql = "DROP TABLE IF EXISTS " + BaseField.TABLE_NAME_PARADETAIL;
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
	
	/*��ѯ���� ������Cursor����*/
	public Cursor query(String whereString) {
		Log.i(BaseField.DATABASE_TAG, "SELECT "+BaseField.TABLE_NAME_PARADETAIL);
		/*������α�Cursor���ص������У�һ��Ҫ��һ����Ϊ��_id��*/
		String sqlString="SELECT detailID as _id, description, infoID FROM "+BaseField.TABLE_NAME_PARADETAIL; 
		if(!whereString.equals("")){
			sqlString+=" "+whereString;
		}
		return db.rawQuery(sqlString, null);
	}
	/*��ѯ���ݣ� ����key-value List����*/
	public List<KeyValue> queryKeyValueList(String whereString){
		List<KeyValue> items=new ArrayList<KeyValue>();
		Cursor cursor=query(whereString);
		if(cursor.getCount()>0){
			while(cursor.moveToNext()){
				KeyValue item=new KeyValue();
				item.setKey(cursor.getInt(cursor.getColumnIndex("_id")));
				item.setValue(cursor.getString(cursor.getColumnIndex("description")));
				items.add(item);
			}
		}
		return items;
	}
}


