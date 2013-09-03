package com.example.mobilefamilyportal;

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
import android.widget.Toast;

public class MainActivity extends Activity {

	private ImageButton dailyConsumeImageButton=null;
	private ImageButton userInfoImageButton=null;
	private ImageButton loginImageButton=null;
	private ImageButton bankImageButton=null;
	private ImageButton othersImageButton=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*�ճ�����*/
		dailyConsumeImageButton=(ImageButton)findViewById(R.id.dailyConsumeImageButton);
		dailyConsumeImageButton.setOnTouchListener(onMyOnTouchListener);
		dailyConsumeImageButton.setOnClickListener(onMyClickListener);
		/*��¼*/
		loginImageButton=(ImageButton)findViewById(R.id.loginImageButton);
		loginImageButton.setOnTouchListener(onMyOnTouchListener);
		loginImageButton.setOnClickListener(onMyClickListener);
		/*�û���Ϣ*/
		userInfoImageButton=(ImageButton)findViewById(R.id.userInfoImageButton);
		userInfoImageButton.setOnTouchListener(onMyOnTouchListener);
		userInfoImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(MainActivity.this, UserInfoActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		/*����*/
		bankImageButton=(ImageButton)findViewById(R.id.bankImageButton);
		bankImageButton.setOnTouchListener(onMyOnTouchListener);
		bankImageButton.setOnClickListener(onMyClickListener);
		/*����*/
		othersImageButton=(ImageButton)findViewById(R.id.othersImageButton);
		othersImageButton.setOnTouchListener(onMyOnTouchListener);
		othersImageButton.setOnClickListener(onMyClickListener);
	}
	/*��ʾģ�����ڽ���*/
	private OnClickListener onMyClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(MainActivity.this, R.string.module_build, Toast.LENGTH_LONG)
			.show();
		}
	};
	/*���¡��ɿ�ImageButton�޸�ImageButton������ɫ*/
	private OnTouchListener onMyOnTouchListener=new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){//��ť������
				v.setBackgroundColor(getResources().getColor(R.color.button_mouseover_color));
			}else if(event.getAction()==MotionEvent.ACTION_UP){//��ť���ɿ�
				v.setBackgroundColor(getResources().getColor(R.color.transparent));
			}
			return false;
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/*�ر�Activity*/ 
	@Override   
    public boolean onKeyDown(int keyCode, KeyEvent event){  
        if(keyCode==KeyEvent.KEYCODE_BACK){  
            this.finish();  
        }  
        return super.onKeyDown(keyCode, event);  
    }

}
