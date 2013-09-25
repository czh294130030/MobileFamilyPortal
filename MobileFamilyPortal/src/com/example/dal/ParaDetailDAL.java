package com.example.dal;

import java.util.ArrayList;
import java.util.List;
import com.example.base.BaseField;
import com.example.model.KeyValue;
import com.example.model.ParaDetail;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ParaDetailDAL extends SQLiteOpenHelper {

	private SQLiteDatabase db;//数据库
	public ParaDetailDAL(Context context) {
		super(context, BaseField.DATABASE_NAME, null, BaseField.DATABASEVERSION);
		db = this.getWritableDatabase();// 打开或新建数据库(第一次时创建)获得SQLiteDatabase对象，为了读取和写入数据
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	/*更新数据库*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(BaseField.DATABASE_TAG, "RECREATE TABLE "+BaseField.TABLE_NAME_PARADETAIL);
		String sql = "DROP TABLE IF EXISTS " + BaseField.TABLE_NAME_PARADETAIL;
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
	
	/*查询数据 ，返回Cursor对象*/
	public Cursor query(String whereString) {
		Log.i(BaseField.DATABASE_TAG, "SELECT "+BaseField.TABLE_NAME_PARADETAIL);
		/*结果集游标Cursor返回的数据中，一定要有一列名为“_id”*/
		String sqlString="SELECT detailID as _id, description, infoID FROM "+BaseField.TABLE_NAME_PARADETAIL; 
		if(!whereString.equals("")){
			sqlString+=" "+whereString;
		}
		return db.rawQuery(sqlString, null);
	}
	/*根据条件获取Model*/
	public ParaDetail queryModel(ParaDetail item){
		ParaDetail model=new ParaDetail();
		Log.i(BaseField.DATABASE_TAG, "SELECT One "+BaseField.TABLE_NAME_PARADETAIL);
		String sql="SELECT detailID as _id, description, infoID FROM "+BaseField.TABLE_NAME_PARADETAIL+" WHERE 1=1";
		if(item.getDetailID()!=0){
			sql+=" and _id="+item.getDetailID();
		}
		if(item.getDescription()!=null){
			sql+=" and description='"+item.getDescription()+"'";
		}
		if(item.getInfoID()!=0){
			sql+=" and infoID="+item.getInfoID();
		}
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext()){
				model.setDetailID(cursor.getInt(cursor.getColumnIndex("_id")));
				model.setDescription(cursor.getString(cursor.getColumnIndex("description")));
				model.setInfoID(cursor.getInt(cursor.getColumnIndex("infoID")));
			}
			return model;
		}else{
			return null;
		}
	}
	/*查询数据， 返回key-value List对象*/
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


