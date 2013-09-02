package com.example.mobilefamilyportal;

import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.dal.UserInfoDAL;
import com.example.model.UserInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class UserInfoActivityOP extends Activity {

	private TextView titleTextView=null;
	private EditText accountEditText=null;
	private EditText usernameEditText=null;
	private EditText passwordEditText=null;
	private int op;
	private int id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_userinfoop);
	    titleTextView=(TextView)findViewById(R.id.titleTextView);
	    accountEditText=(EditText)findViewById(R.id.accountEditText);
	    usernameEditText=(EditText)findViewById(R.id.usernameEditText);
	    passwordEditText=(EditText)findViewById(R.id.passwordEditText);
	    Bundle bundle= this.getIntent().getExtras();
	    op= bundle.getInt("op");
	    /*����û���Ϣ*/
	    if(op==BaseField.ADD){titleTextView.setText(R.string.add_userinfo);}
	    /*�޸��û���Ϣ*/
	    else if(op==BaseField.EDIT){
	    	titleTextView.setText(R.string.edit_userinfo);
	    	id=bundle.getInt("id");
	    	bind(id);
	    }
	}

	/*��Ӳ˵�*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0, BaseField.OK, 0, R.string.ok);
		menu.add(0, BaseField.CANCEL, 0, R.string.cancel);
		return super.onCreateOptionsMenu(menu);
	}
	/*�˵������¼�*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case BaseField.OK:
			if(op==BaseField.ADD){
				addUserInfo();
			}else {
				updateUserInfo(id);
			}
			break;
		case BaseField.CANCEL:
			Intent cancelIntent=new Intent();
			setResult(BaseField.ADD_CANCEL, cancelIntent);
			finish();
			break;
		default:
			break;
		}
		return true;
	}
	/*�ر�Activity*/ 
	@Override   
    public boolean onKeyDown(int keyCode, KeyEvent event){  
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	Intent cancelIntent=new Intent();
			setResult(BaseField.ADD_CANCEL, cancelIntent);
            this.finish();  
        }  
        return super.onKeyDown(keyCode, event);  
    }
	/*����id�����û���Ϣ�޸ĵ����ݿ�*/
	private void updateUserInfo(int _id){
		UserInfo model=getUserInfoFromInput();
		if(model!=null){
			model.setUserID(_id);
			UserInfoDAL userInfoDAL=new UserInfoDAL(this);
			int result=userInfoDAL.update(model);
			userInfoDAL.close();
			if(result>0){
				Intent intent=new Intent();
				setResult(BaseField.UPDATE_SUCCESSFULLY, intent);
				finish();
			}else{
				BaseMethod.showInformation(this, R.string.warm_prompt, R.string.update_unsuccessfully);
			}
		}
		
	}
	/*�����û���Ϣ��ӵ����ݿ�*/
	private void addUserInfo(){
		UserInfo model=getUserInfoFromInput();
		if(model!=null){
			UserInfoDAL userInfoDAL=new UserInfoDAL(this);
			long result =userInfoDAL.add(model);
			userInfoDAL.close();
			if(result>0){
				Intent intent=new Intent();
				setResult(BaseField.ADD_SUCCESSFULLY, intent);
				finish();
			}else{
				BaseMethod.showInformation(this, R.string.warm_prompt, R.string.add_unsuccessfully);
			}
		}
	}
	/*����id�����ݿ��ȡ�û���Ϣ���󶨱�*/
	private void bind(int _id){
		UserInfo model=new UserInfo();
		model=getUserInfo(_id);
		if(model!=null){
			accountEditText.setText(model.getAccount());
			usernameEditText.setText(model.getUserName());
			passwordEditText.setText(model.getPassword());
		}
	}
	/*����id�����ݿ��л�ȡ�û���Ϣ*/
	private UserInfo getUserInfo(int _id){
		UserInfo model=new UserInfo();
		UserInfoDAL userInfoDAL=new UserInfoDAL(this);
		model=userInfoDAL.queryUserInfoByID(_id);
		userInfoDAL.close();
		return model;
	}
	/*��ȡ�����û����룩���û���Ϣ*/
	private UserInfo getUserInfoFromInput(){
		if(validateUserInfo()){
			UserInfo model=new UserInfo();
			String account=accountEditText.getText().toString().trim();
			String userName=usernameEditText.getText().toString().trim();
			String password=passwordEditText.getText().toString().trim();
			model.setAccount(account);
			model.setUserName(userName);
			model.setPassword(password);
			return model;
		}else {
			return null;
		}
	}
	/*��֤�����û���Ϣ*/
	private boolean validateUserInfo(){
		String account=accountEditText.getText().toString().trim();
		String userName=usernameEditText.getText().toString().trim();
		String password=passwordEditText.getText().toString().trim();
		if(account.equals("")){
			BaseMethod.showInformation(this, R.string.warm_prompt, R.string.account_required);
			accountEditText.requestFocus();
			return false;
		}
		if(userName.equals("")){
			BaseMethod.showInformation(this, R.string.warm_prompt, R.string.username_required);
			usernameEditText.requestFocus();
			return false;
		}
		if(password.equals("")){
			BaseMethod.showInformation(this, R.string.warm_prompt, R.string.password_required);
			passwordEditText.requestFocus();
			return false;
		}
		return true;
	}
}
