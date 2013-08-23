package com.example.mobilefamilyportal;

import com.example.base.BaseField;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class UserInfoActivityOP extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_userinfoop);
	}

	/*Ìí¼Ó²Ëµ¥*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0, BaseField.OK, 0, R.string.ok);
		menu.add(0, BaseField.Cancel, 0, R.string.cancel);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case BaseField.OK:
			break;
		case BaseField.Cancel:
			this.finish();
			break;
		default:
			break;
		}
		return true;
	}
}
