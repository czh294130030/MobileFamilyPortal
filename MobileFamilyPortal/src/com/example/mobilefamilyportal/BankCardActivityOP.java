package com.example.mobilefamilyportal;

import java.util.List;
import com.example.base.BaseField;
import com.example.dal.ParaDetailDAL;
import com.example.model.KeyValue;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class BankCardActivityOP extends Activity {

	private int op;
	private Spinner cardTypeSpinner=null;
	private Spinner cardCitySpinner=null;
	private Spinner cardUserSpinner=null;
	private long bankID=0;
	private long cityID=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_bankcardop);
	    cardTypeSpinner=(Spinner)findViewById(R.id.cardTypeSpinner);
	    cardTypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
	    	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ 
	    		bankID=((KeyValue)parent.getSelectedItem()).getKey();		
	    	}
	    	@Override  
	    	public void onNothingSelected(AdapterView<?> arg0) {  
	    	} 
		});
	    cardCitySpinner=(Spinner)findViewById(R.id.cardCitySpinner);
	    cardCitySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
	    	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ 
	    		cityID=((KeyValue)parent.getSelectedItem()).getKey();		
	    	}
	    	@Override  
	    	public void onNothingSelected(AdapterView<?> arg0) {  
	    	} 
		});
	    cardUserSpinner=(Spinner)findViewById(R.id.cardUserSpinner);
	    Bundle bundle=this.getIntent().getExtras();
	    op=bundle.getInt("op");
	    /*添加银行卡*/
	    if(op==BaseField.ADD){
	    	bindSpinner();
	    }
	    /*修改银行卡*/
	    if(op==BaseField.EDIT){
	    	
	    }
	}
	/*绑定银行卡类型、城市、所有者*/
	private void bindSpinner(){
		bindCardType();
		bindCardCity();
		bindCardUser();
	}
	/*绑定银行卡类型*/
	private void bindCardType(){
		ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
		List<KeyValue> items=paraDetailDAL.queryKeyValueList("WHERE infoID="+BaseField.CARD_TYPE);
		ArrayAdapter<KeyValue> adapter=new ArrayAdapter<KeyValue>(
				this, 
				android.R.layout.simple_spinner_item, 
				items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		cardTypeSpinner.setAdapter(adapter);
		paraDetailDAL.close();
	}
	/*绑定银行卡*/
	private void bindCardCity(){
		ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
		List<KeyValue> items=paraDetailDAL.queryKeyValueList("WHERE infoID="+BaseField.CARD_CITY);
		ArrayAdapter<KeyValue> adapter=new ArrayAdapter<KeyValue>(
				this, 
				android.R.layout.simple_spinner_item, 
				items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		cardCitySpinner.setAdapter(adapter);
		paraDetailDAL.close();
	}
	/*绑定银行卡所有者*/
	private void bindCardUser(){
	}
}
