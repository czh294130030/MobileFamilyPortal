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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class BankCardActivityOP extends Activity {

	private int op;
	private int id;
	private Spinner cardTypeSpinner=null;
	private Spinner cardCitySpinner=null;
	private Spinner cardUserSpinner=null;
	private EditText cardNOEditText=null; 
	private TextView titleTextView=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_bankcardop);
	    titleTextView=(TextView)findViewById(R.id.titleTextView);
	    cardNOEditText=(EditText)findViewById(R.id.cardNOEditText);
	    cardTypeSpinner=(Spinner)findViewById(R.id.cardTypeSpinner);
	    cardCitySpinner=(Spinner)findViewById(R.id.cardCitySpinner);
	    cardUserSpinner=(Spinner)findViewById(R.id.cardUserSpinner);
	    Bundle bundle=this.getIntent().getExtras();
	    op=bundle.getInt("op");
	    /*������п�*/
	    if(op==BaseField.ADD){
	    	bindSpinner(0, 0, 0);
	    }
	    /*�޸����п�*/
	    if(op==BaseField.EDIT){
	    	id=bundle.getInt("id");
	    	titleTextView.setText(R.string.edit_bank_card);
	    	bind(id);
	    }
	}
	/*�������п���Ż�ȡ���п���Ϣ�󶨱�*/
	private void bind(int _id){
		BankCard model=getBankCard(_id);
		if(model!=null){
			cardNOEditText.setText(model.getCardNO());
			bindSpinner(model.getBankID(), model.getCityID(), model.getUserID());
		}
	}
	/*�������п���Ż�ȡ���п�*/
	private BankCard getBankCard(int _id){
		BankCard item=new BankCard();
		item.setCardID(_id);
		BankCardDAL bankCardDAL=new BankCardDAL(this);
		BankCard model=bankCardDAL.queryModel(item);
		bankCardDAL.close();
		return model;
	}
	/*��Ӳ˵�*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		BaseMethod.setIconEnable(menu, true);//���android 4.0��ʾͼƬ
		menu.add(0, BaseField.OK, 0, R.string.ok).setIcon(R.drawable.menu_ok);
		menu.add(0, BaseField.CANCEL, 0, R.string.cancel).setIcon(R.drawable.menu_cancel);
		return super.onCreateOptionsMenu(menu);
	}
	/*�˵������¼�*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case BaseField.OK:
			if(op==BaseField.ADD){
				addBankCard();
			}else{
				updateBankCard(id);
			}
			break;
		case BaseField.CANCEL:
			unsaveChanges();
			break;
		default:
			break;
		}
		return true;
	}
	/*�޸����п���Ϣ*/
	private void updateBankCard(int _id){
		BankCard model=getBankCardFromInput();
		if(model!=null){
			model.setCardID(_id);
			BankCardDAL bankCardDAL=new BankCardDAL(this);
			long result=bankCardDAL.update(model);
			bankCardDAL.close();
			if(result>0){
				Intent intent=new Intent();
				setResult(BaseField.UPDATE_SUCCESSFULLY, intent);
				finish();
			}else{
				BaseMethod.showInformation(this, R.string.warm_prompt, R.string.update_unsuccessfully);
			}
		}
	}
	/*������п���Ϣ*/
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
	/*�ӱ����û���������п���Ϣ*/
	private BankCard getBankCardFromInput(){
		if(validateBankCard()){
			BankCard model=new BankCard();
			String cardNO=cardNOEditText.getText().toString().trim();
			model.setCardNO(cardNO);
			model.setBankID(((KeyValue)cardTypeSpinner.getSelectedItem()).getKey());
			model.setCityID(((KeyValue)cardCitySpinner.getSelectedItem()).getKey());
			model.setUserID(((KeyValue)cardUserSpinner.getSelectedItem()).getKey());
			return model;
		}else{
			return null;
		}
	}
	/*��֤�����п���Ϣ*/
	private boolean validateBankCard(){
		String cardNO=cardNOEditText.getText().toString().trim();
		if(cardNO.equals("")){
			BaseMethod.showInformation(this, R.string.warm_prompt, R.string.card_no_required);
			cardNOEditText.requestFocus();
			return false;
		}
		return true;
	}
	/*�ر�Activity*/ 
	@Override   
    public boolean onKeyDown(int keyCode, KeyEvent event){  
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	unsaveChanges();
        }  
        return super.onKeyDown(keyCode, event);  
    }
	/*��������ת���û���Ϣҳ��*/
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
	/*�����п����͡����С�������*/
	private void bindSpinner(int _bankID, int _cityID, int _userID){
		bindCardType(_bankID);
		bindCardCity(_cityID);
		bindCardUser(_userID);
	}
	/*�����п�����*/
	private void bindCardType(int _bankID){
		ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
		List<KeyValue> items=paraDetailDAL.queryKeyValueList("WHERE infoID="+BaseField.CARD_TYPE);
		if(items.size()>0){
			int selectedPosition=0;
			if(_bankID!=0){//�޸�
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
	/*�����п�*/
	private void bindCardCity(int _cityID){
		ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
		List<KeyValue> items=paraDetailDAL.queryKeyValueList("WHERE infoID="+BaseField.CARD_CITY);
		if(items.size()>0){
			int selectedPosition=0;
			if(_cityID!=0){//�޸�
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
	/*�����п�������*/
	private void bindCardUser(int _userID){
		UserInfoDAL userInfoDAL=new UserInfoDAL(this);
		List<KeyValue> items=userInfoDAL.queryKeyValueList("");
		if(items.size()>0){
			int selectedPosition=0;
			if(_userID!=0){//�޸�
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
