package com.example.mobilefamilyportal;

import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.dal.UserInfoDAL;
import com.example.model.UserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private EditText accountEditText=null;
	private EditText passwordEditText=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    accountEditText=(EditText)findViewById(R.id.accountEditText);
	    passwordEditText=(EditText)findViewById(R.id.passwordEditText);
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
			Login();
			break;
		case BaseField.CANCEL:
			leaveLoginPage();
			break;
		default:
			break;
		}
		return true;
	}
	/*登录*/
	private void Login(){
		if(validateLogin()){
			String account=accountEditText.getText().toString().trim();
			String password=passwordEditText.getText().toString().trim();
			if(!account.equals("")&&!password.equals("")){
				UserInfoDAL userInfoDAL=new UserInfoDAL(this);
				UserInfo model=userInfoDAL.queryUserInfoByAccountAndPassword(account, password);
				userInfoDAL.close();
				if(model!=null){
					Intent intent=new Intent();
					setResult(BaseField.LOGIN_SUCCESSFULLY, intent);
					finish();
				}else{
					BaseMethod.showInformation(this, R.string.warm_prompt, R.string.login_unsuccessfully);
				}
			}
		}
	}
	/*检测用户输入的登录信息*/
	private boolean validateLogin(){
		String account=accountEditText.getText().toString().trim();
		String password=passwordEditText.getText().toString().trim();
		if(account.equals("")){
			BaseMethod.showInformation(this, R.string.warm_prompt, R.string.account_required);
			accountEditText.requestFocus();
			return false;
		}
		if(password.equals("")){
			BaseMethod.showInformation(this, R.string.warm_prompt, R.string.password_required);
			passwordEditText.requestFocus();
			return false;
		}
		return true;
	}
	/*关闭Activity*/ 
	@Override   
    public boolean onKeyDown(int keyCode, KeyEvent event){  
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	leaveLoginPage();
        }  
        return super.onKeyDown(keyCode, event);  
    }
	/*放弃登录跳转到主界面*/
	private void leaveLoginPage(){
		new AlertDialog.Builder(this)
    	.setTitle(R.string.warm_prompt)
    	.setIcon(R.drawable.alert_info)
    	.setMessage(R.string.leave_login)
    	.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent cancelIntent=new Intent();
				setResult(BaseField.LOGIN_CANCEL, cancelIntent);
	            finish();  
			}
		}).setNegativeButton(R.string.cancel, null)
		.show();
	}
}
