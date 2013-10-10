package com.example.mobilefamilyportal;

import com.example.base.BaseField;
import com.example.base.BaseMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DailyConsumeActivity extends Activity {

	/*用来控制菜单来是添加还是修改、删除，初始化默认添加菜单*/
	private boolean isAdd=true;
	private String TAG="MENU_DAILY_CONSUME";
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_dailyconsume);
	}
	/* 1. 当手机(Emulator)sdk版本>=11（如我的手机Android Version是4.1.1, Build.VERSION.SDK_INT是16） 
     * 在创建Activity时触发。 
     * 2. 当手机(Emulator)sdk版本<11(如我的手机Android Version是2.3.4，Build.VERSION.SDK_INT是10) 
     * 在第一次单击Menu时触发。 
     */  
    public boolean onCreateOptionsMenu(Menu menu) {  
    	Log.i(TAG, "onCreateOptionsMenu");
		BaseMethod.setIconEnable(menu, true);//解决android 4.0显示图片
		menu.add(0, BaseField.ADD, 0, R.string.add_item).setIcon(R.drawable.menu_add);
		menu.add(0, BaseField.EDIT, 1, R.string.edit_item).setIcon(R.drawable.menu_edit);
		menu.add(0, BaseField.DELETE, 2, R.string.delete_item).setIcon(R.drawable.menu_delete);
		menu.add(0, BaseField.VIEW, 3, R.string.view_item).setIcon(R.drawable.menu_view);
		return true;
    }  
    /* 1. 当手机(Emulator)sdk版本>=11（如我的手机Android Version是4.1.1, Build.VERSION.SDK_INT是16） 
     * 在创建Activity触发onCreateOptionsMenu后触发， 
     * 第一次单击Menu时不触发，之后每次单机Menu时触发。 
     * 2. 当手机(Emulator)sdk版本<11(如我的手机Android Version是2.3.4，Build.VERSION.SDK_INT是10) 
     * 在每次单击Menu时触发。 
     * */  
    @Override  
    public boolean onPrepareOptionsMenu(Menu menu) {  
    	Log.i(TAG, "onPrepareOptionsMenu");
		menu.findItem(BaseField.ADD).setVisible(false);
		menu.findItem(BaseField.EDIT).setVisible(false);
		menu.findItem(BaseField.DELETE).setVisible(false);
		menu.findItem(BaseField.VIEW).setVisible(false);
		if(isAdd){//添加
			menu.findItem(BaseField.ADD).setVisible(true);
		}else{//删除,修改,查看
			menu.findItem(BaseField.EDIT).setVisible(true);
			menu.findItem(BaseField.DELETE).setVisible(true);
			menu.findItem(BaseField.VIEW).setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);
    } 
    /*在关闭菜单时出发*/
	@Override
	public void onOptionsMenuClosed(Menu menu){
		Log.i(TAG, "onOptionsMenuClosed");
		isAdd=true;
	}
	/*在单击菜单选项时触发*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Log.i(TAG, "onOptionsItemSelected");
		switch (item.getItemId()) {
		case BaseField.ADD:{
			Intent intent=new Intent(DailyConsumeActivity.this, DailyConsumeActivityOP.class);
			Bundle bundle=new Bundle();
    		bundle.putInt("op", BaseField.ADD);
    		intent.putExtras(bundle);
			startActivityForResult(intent, BaseField.ADD_DAILY_CONSUME);
			break;}
		case BaseField.EDIT:{
    		/*Intent intent=new Intent(DailyConsumeActivity.this, UserInfoActivityOP.class);
    		Bundle bundle=new Bundle();
    		bundle.putInt("op", BaseField.EDIT);
    		bundle.putInt("id", id);
    		intent.putExtras(bundle);
    		startActivityForResult(intent, BaseField.EDIT_USERINFO);*/
			break;}
		case BaseField.VIEW:{
			break;
		}
		case BaseField.DELETE:
			/*deleteUserInfo(id);*/
			break;
		default:
			break;
		}
		return true;
	}
}
