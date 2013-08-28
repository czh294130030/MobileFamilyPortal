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
import android.widget.TextView;

public class UserInfoActivityOP extends Activity {

	private TextView titleTextView=null;
	private EditText accountEditText=null;
	private EditText usernameEditText=null;
	private EditText passwordEditText=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_userinfoop);
	    titleTextView=(TextView)findViewById(R.id.titleTextView);
	    accountEditText=(EditText)findViewById(R.id.accountEditText);
	    usernameEditText=(EditText)findViewById(R.id.usernameEditText);
	    passwordEditText=(EditText)findViewById(R.id.passwordEditText);
	    Bundle bundle= this.getIntent().getExtras();
	    int op= bundle.getInt("op");
	    /*添加用户信息*/
	    if(op==BaseField.ADD){titleTextView.setText(R.string.add_userinfo);}
	    /*修改用户信息*/
	    else if(op==BaseField.EDIT){
	    	titleTextView.setText(R.string.edit_userinfo);
	    	int id=bundle.getInt("id");
	    	bind(id);
	    }
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
			UserInfo model=validateUserInfo();
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
	/*根据id绑定表单*/
	private void bind(int _id){
		UserInfo model=new UserInfo();
		model=getUserInfo(_id);
		if(model!=null){
			accountEditText.setText(model.getAccount());
			usernameEditText.setText(model.getUserName());
			passwordEditText.setText(model.getPassword());
		}
	}
	/*根据id从数据库中获取用户信息*/
	private UserInfo getUserInfo(int _id){
		UserInfo model=new UserInfo();
		UserInfoDAL userInfoDAL=new UserInfoDAL(this);
		model=userInfoDAL.queryUserInfoByID(_id);
		userInfoDAL.close();
		return model;
	}
	/*验证表单数据并返回*/
	private UserInfo validateUserInfo(){
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
