package com.example.mobilefamilyportal;

import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.dal.UserInfoDAL;
import com.example.model.UserInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class UserInfoActivityOP extends Activity {

	private EditText accountEditText=null;
	private EditText usernameEditText=null;
	private EditText passwordEditText=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_userinfoop);
	    accountEditText=(EditText)findViewById(R.id.accountEditText);
	    usernameEditText=(EditText)findViewById(R.id.usernameEditText);
	    passwordEditText=(EditText)findViewById(R.id.passwordEditText);
	}

	/*添加菜单*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0, BaseField.OK, 0, R.string.ok);
		menu.add(0, BaseField.CANCEL, 0, R.string.cancel);
		return super.onCreateOptionsMenu(menu);
	}
	/*菜单触发事件*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case BaseField.OK:
			UserInfo model=getUserInfo();
			if(model!=null){
				UserInfoDAL userInfoDAL=new UserInfoDAL(this);
				long result =userInfoDAL.add(model);
				if(result>0){
					Intent okIntent=new Intent();
					setResult(BaseField.ADD_SUCCESSFULLY, okIntent);
					finish();
				}else{
					BaseMethod.ShowInformation(this, R.string.warm_prompt, R.string.add_unsuccessfully);
				}
				userInfoDAL.close();
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
	/*验证输入数据*/
	private UserInfo getUserInfo(){
		UserInfo model=new UserInfo();
		String account=accountEditText.getText().toString().trim();
		String userName=usernameEditText.getText().toString().trim();
		String password=passwordEditText.getText().toString().trim();
		if(account.equals("")){
			BaseMethod.ShowInformation(this, R.string.warm_prompt, R.string.account_required);
			accountEditText.requestFocus();
			return null;
		}
		if(userName.equals("")){
			BaseMethod.ShowInformation(this, R.string.warm_prompt, R.string.username_required);
			usernameEditText.requestFocus();
			return null;
		}
		if(password.equals("")){
			BaseMethod.ShowInformation(this, R.string.warm_prompt, R.string.password_required);
			passwordEditText.requestFocus();
			return null;
		}
		model.setAccount(account);
		model.setUserName(userName);
		model.setPassword(password);
		return model;
	}
}
