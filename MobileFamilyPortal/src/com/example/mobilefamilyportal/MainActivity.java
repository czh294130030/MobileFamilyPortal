package com.example.mobilefamilyportal;

import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.dal.ParaInfoDAL;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView welcomeTextView=null;
	private ImageButton dailyConsumeImageButton=null;
	private ImageButton userInfoImageButton=null;
	private ImageButton loginImageButton=null;
	private ImageButton bankImageButton=null;
	private ImageButton settingsImageButton=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		welcomeTextView=(TextView)findViewById(R.id.welcomeTextView);
		showWelcomeInfo();
		/*创建所有表和内置数据*/
		ParaInfoDAL paraInfoDAL=new ParaInfoDAL(this);
		paraInfoDAL.close();
		/*日常消费*/
		dailyConsumeImageButton=(ImageButton)findViewById(R.id.dailyConsumeImageButton);
		dailyConsumeImageButton.setOnTouchListener(onMyOnTouchListener);
		dailyConsumeImageButton.setOnClickListener(onMyClickListener);
		/*登录*/
		loginImageButton=(ImageButton)findViewById(R.id.loginImageButton);
		loginImageButton.setOnTouchListener(onMyOnTouchListener);
		loginImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,LoginActivity.class);
				startActivityForResult(intent, BaseField.LOGIN);
			}
		});
		/*用户信息*/
		userInfoImageButton=(ImageButton)findViewById(R.id.userInfoImageButton);
		userInfoImageButton.setOnTouchListener(onMyOnTouchListener);
		userInfoImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(BaseField.LOGIN_USER_ID==BaseField.ADMIN_USER_ID){/*登录的用户是管理员*/
					Intent intent=new Intent(MainActivity.this, UserInfoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				else{
					BaseMethod.showInformation(MainActivity.this, R.string.warm_prompt, R.string.login_as_administrator);
				}
			}
		});
		/*银行卡*/
		bankImageButton=(ImageButton)findViewById(R.id.bankImageButton);
		bankImageButton.setOnTouchListener(onMyOnTouchListener);
		bankImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(BaseField.LOGIN_USER_ID!=0&&BaseField.LOGIN_USER_ID!=BaseField.ADMIN_USER_ID){/*登录用户是普通用户*/
					Intent intent=new Intent(MainActivity.this, BankCardActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				else{
					BaseMethod.showInformation(MainActivity.this, R.string.warm_prompt, R.string.login_as_normal_user);
				}
			}
		});
		/*设置*/
		settingsImageButton=(ImageButton)findViewById(R.id.settingsImageButton);
		settingsImageButton.setOnTouchListener(onMyOnTouchListener);
		settingsImageButton.setOnClickListener(onMyClickListener);
	}
	/* 重写onActivityResult这个方法  
     * 是要等到LoginActivity finish后才会执行的  
     */    
    @Override    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if(requestCode==BaseField.LOGIN&&resultCode==BaseField.LOGIN_SUCCESSFULLY){
    		Bundle bundle=data.getExtras();
    		BaseField.LOGIN_USER_ID=bundle.getInt("userID");
    		BaseField.LOGIN_USER_NAME=bundle.getString("userName");
    		showWelcomeInfo();
    	}
    	super.onActivityResult(requestCode, resultCode, data);  
    }
    /*显示欢迎信息*/
    private void showWelcomeInfo(){
    	if(BaseField.LOGIN_USER_NAME.equals("")){
    		welcomeTextView.setText("guest welcome");
    	}else {
			welcomeTextView.setText(BaseField.LOGIN_USER_NAME+" welcome");
		}
    }
	/*显示模块正在建设*/
	private OnClickListener onMyClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(MainActivity.this, R.string.module_build, Toast.LENGTH_SHORT)
			.show();
		}
	};
	/*按下、松开ImageButton修改ImageButton背景颜色*/
	private OnTouchListener onMyOnTouchListener=new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){//按钮被按下
				v.setBackgroundColor(getResources().getColor(R.color.button_mouseover_color));
			}else if(event.getAction()==MotionEvent.ACTION_UP){//按钮被松开
				v.setBackgroundColor(getResources().getColor(R.color.transparent));
			}
			return false;
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/*关闭Activity*/ 
	@Override   
    public boolean onKeyDown(int keyCode, KeyEvent event){  
        if(keyCode==KeyEvent.KEYCODE_BACK){  
            this.finish();  
        }  
        return super.onKeyDown(keyCode, event);  
    }

}
