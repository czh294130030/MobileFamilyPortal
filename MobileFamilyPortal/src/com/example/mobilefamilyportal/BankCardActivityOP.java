package com.example.mobilefamilyportal;

import java.util.List;
import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.dal.BankCardDAL;
import com.example.dal.ParaDetailDAL;
import com.example.dal.UserInfoDAL;
import com.example.model.BankCard;
import com.example.model.KeyValue;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class BankCardActivityOP extends Activity {

	private int op;
	private int id;
	private Spinner cardTypeSpinner=null;
	private Spinner cardCitySpinner=null;
	private Spinner cardUserSpinner=null;
	private EditText cardNOEditText=null; 
	private long bankID=0;
	private long cityID=0;
	private long userID=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_bankcardop);
	    cardNOEditText=(EditText)findViewById(R.id.cardNOEditText);
	    /*获取选中的银行ID*/
	    cardTypeSpinner=(Spinner)findViewById(R.id.cardTypeSpinner);
	    cardTypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
	    	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ 
	    		bankID=((KeyValue)parent.getSelectedItem()).getKey();		
	    	}
	    	@Override  
	    	public void onNothingSelected(AdapterView<?> arg0) {  
	    	} 
		});
	    /*获取选中的城市ID*/
	    cardCitySpinner=(Spinner)findViewById(R.id.cardCitySpinner);
	    cardCitySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
	    	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ 
	    		cityID=((KeyValue)parent.getSelectedItem()).getKey();		
	    	}
	    	@Override  
	    	public void onNothingSelected(AdapterView<?> arg0) {  
	    	} 
		});
	    /*获取选中的用户ID*/
	    cardUserSpinner=(Spinner)findViewById(R.id.cardUserSpinner);
	    cardUserSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
	    	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ 
	    		userID=((KeyValue)parent.getSelectedItem()).getKey();		
	    	}
	    	@Override  
	    	public void onNothingSelected(AdapterView<?> arg0) {  
	    	} 
		});
	    Bundle bundle=this.getIntent().getExtras();
	    op=bundle.getInt("op");
	    /*添加银行卡*/
	    if(op==BaseField.ADD){
	    	bindSpinner(Integer.parseInt(String.valueOf(bankID)), 
	    					Integer.parseInt(String.valueOf(cityID)), 
	    						Integer.parseInt(String.valueOf(userID)));
	    	
	    }
	    /*修改银行卡*/
	    if(op==BaseField.EDIT){
	    	id=bundle.getInt("id");
	    	bind(id);
	    }
	}
	/*根据银行卡编号获取银行卡信息绑定表单*/
	private void bind(int _id){
		BankCard model=getBankCard(_id);
		if(model!=null){
			cardNOEditText.setText(model.getCardNO());
			bankID=model.getBankID();
			cityID=model.getCityID();
			userID=model.getUserID();
			bindSpinner(Integer.parseInt(String.valueOf(bankID)), 
					Integer.parseInt(String.valueOf(cityID)), 
						Integer.parseInt(String.valueOf(userID)));
		}
	}
	/*根据银行卡编号获取银行卡*/
	private BankCard getBankCard(int _id){
		BankCard item=new BankCard();
		item.setCardID(_id);
		BankCardDAL bankCardDAL=new BankCardDAL(this);
		BankCard model=bankCardDAL.queryModel(item);
		bankCardDAL.close();
		return model;
	}
	/*添加菜单*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		BaseMethod.setIconEnable(menu, true);//解决android 4.0显示图片
		menu.add(0, BaseField.OK, 0, R.string.ok).setIcon(R.drawable.menu_ok);
		menu.add(0, BaseField.CANCEL, 0, R.string.cancel).setIcon(R.drawable.menu_cancel);
		return super.onCreateOptionsMenu(menu);
	}
	/*菜单触发事件*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case BaseField.OK:
			addBankCard();
			break;
		case BaseField.CANCEL:
			unsaveChanges();
			break;
		default:
			break;
		}
		return true;
	}
	/*添加银行卡信息*/
	private void addBankCard(){
		BankCard model=getBankCardFromInput();
		if(model!=null){
			BankCardDAL bankCardDAL=new BankCardDAL(this);
			long result=bankCardDAL.add(model);
			bankCardDAL.close();
			if(result>0){
				Intent intent=new Intent();
				setResult(BaseField.ADD_SUCCESSFULLY, intent);
				finish();
			}else{
				BaseMethod.showInformation(this, R.string.warm_prompt, R.string.add_unsuccessfully);
			}
		}
	}
	/*从表单或用户输入的银行卡信息*/
	private BankCard getBankCardFromInput(){
		if(validateBankCard()){
			BankCard model=new BankCard();
			String cardNO=cardNOEditText.getText().toString().trim();
			model.setCardNO(cardNO);
			model.setBankID(Integer.parseInt(String.valueOf(bankID)));
			model.setCityID(Integer.parseInt(String.valueOf(cityID)));
			model.setUserID(Integer.parseInt(String.valueOf(userID)));
			return model;
		}else{
			return null;
		}
	}
	/*验证表单银行卡信息*/
	private boolean validateBankCard(){
		String cardNO=cardNOEditText.getText().toString().trim();
		if(cardNO.equals("")){
			BaseMethod.showInformation(this, R.string.warm_prompt, R.string.card_no_required);
			cardNOEditText.requestFocus();
			return false;
		}
		return true;
	}
	/*关闭Activity*/ 
	@Override   
    public boolean onKeyDown(int keyCode, KeyEvent event){  
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	unsaveChanges();
        }  
        return super.onKeyDown(keyCode, event);  
    }
	/*不保存跳转到用户信息页面*/
	private void unsaveChanges(){
		new AlertDialog.Builder(this)
    	.setTitle(R.string.warm_prompt)
    	.setIcon(R.drawable.alert_info)
    	.setMessage(R.string.unsave_changes)
    	.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent cancelIntent=new Intent();
				setResult(BaseField.ADD_CANCEL, cancelIntent);
	            finish();  
			}
		}).setNegativeButton(R.string.cancel, null)
		.show();
	}
	/*绑定银行卡类型、城市、所有者*/
	private void bindSpinner(int _bankID, int _cityID, int _userID){
		bindCardType(_bankID);
		bindCardCity(_cityID);
		bindCardUser(_userID);
	}
	/*绑定银行卡类型*/
	private void bindCardType(int _bankID){
		ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
		List<KeyValue> items=paraDetailDAL.queryKeyValueList("WHERE infoID="+BaseField.CARD_TYPE);
		if(items.size()>0){
			int selectedPosition=0;
			if(_bankID!=0){//修改
				for (KeyValue item : items) {
					if(item.getKey()!=_bankID){selectedPosition++;}
					else{break;}
				}
			}
			ArrayAdapter<KeyValue> adapter=new ArrayAdapter<KeyValue>(
					this, 
					android.R.layout.simple_spinner_item, 
					items);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
			cardTypeSpinner.setAdapter(adapter);
			cardTypeSpinner.setSelection(selectedPosition);
			paraDetailDAL.close();
		}
	}
	/*绑定银行卡*/
	private void bindCardCity(int _cityID){
		ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
		List<KeyValue> items=paraDetailDAL.queryKeyValueList("WHERE infoID="+BaseField.CARD_CITY);
		if(items.size()>0){
			int selectedPosition=0;
			if(_cityID!=0){//修改
				for (KeyValue item : items) {
					if(item.getKey()!=_cityID){selectedPosition++;}
					else{break;}
				}
			}
			ArrayAdapter<KeyValue> adapter=new ArrayAdapter<KeyValue>(
					this, 
					android.R.layout.simple_spinner_item, 
					items);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
			cardCitySpinner.setAdapter(adapter);
			cardCitySpinner.setSelection(selectedPosition);
			paraDetailDAL.close();
		}
	}
	/*绑定银行卡所有者*/
	private void bindCardUser(int _userID){
		UserInfoDAL userInfoDAL=new UserInfoDAL(this);
		List<KeyValue> items=userInfoDAL.queryKeyValueList("");
		if(items.size()>0){
			int selectedPosition=0;
			if(_userID!=0){//修改
				for (KeyValue item : items) {
					if(item.getKey()!=_userID){selectedPosition++;}
					else{break;}
				}
			}
			ArrayAdapter<KeyValue> adapter=new ArrayAdapter<KeyValue>(
					this, 
					android.R.layout.simple_spinner_item, 
					items);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			cardUserSpinner.setAdapter(adapter);
			cardUserSpinner.setSelection(selectedPosition);
			userInfoDAL.close();
		}
	}
}
