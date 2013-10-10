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

	/*�������Ʋ˵�������ӻ����޸ġ�ɾ������ʼ��Ĭ����Ӳ˵�*/
	private boolean isAdd=true;
	private String TAG="MENU_DAILY_CONSUME";
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_dailyconsume);
	}
	/* 1. ���ֻ�(Emulator)sdk�汾>=11�����ҵ��ֻ�Android Version��4.1.1, Build.VERSION.SDK_INT��16�� 
     * �ڴ���Activityʱ������ 
     * 2. ���ֻ�(Emulator)sdk�汾<11(���ҵ��ֻ�Android Version��2.3.4��Build.VERSION.SDK_INT��10) 
     * �ڵ�һ�ε���Menuʱ������ 
     */  
    public boolean onCreateOptionsMenu(Menu menu) {  
    	Log.i(TAG, "onCreateOptionsMenu");
		BaseMethod.setIconEnable(menu, true);//���android 4.0��ʾͼƬ
		menu.add(0, BaseField.ADD, 0, R.string.add_item).setIcon(R.drawable.menu_add);
		menu.add(0, BaseField.EDIT, 1, R.string.edit_item).setIcon(R.drawable.menu_edit);
		menu.add(0, BaseField.DELETE, 2, R.string.delete_item).setIcon(R.drawable.menu_delete);
		menu.add(0, BaseField.VIEW, 3, R.string.view_item).setIcon(R.drawable.menu_view);
		return true;
    }  
    /* 1. ���ֻ�(Emulator)sdk�汾>=11�����ҵ��ֻ�Android Version��4.1.1, Build.VERSION.SDK_INT��16�� 
     * �ڴ���Activity����onCreateOptionsMenu�󴥷��� 
     * ��һ�ε���Menuʱ��������֮��ÿ�ε���Menuʱ������ 
     * 2. ���ֻ�(Emulator)sdk�汾<11(���ҵ��ֻ�Android Version��2.3.4��Build.VERSION.SDK_INT��10) 
     * ��ÿ�ε���Menuʱ������ 
     * */  
    @Override  
    public boolean onPrepareOptionsMenu(Menu menu) {  
    	Log.i(TAG, "onPrepareOptionsMenu");
		menu.findItem(BaseField.ADD).setVisible(false);
		menu.findItem(BaseField.EDIT).setVisible(false);
		menu.findItem(BaseField.DELETE).setVisible(false);
		menu.findItem(BaseField.VIEW).setVisible(false);
		if(isAdd){//���
			menu.findItem(BaseField.ADD).setVisible(true);
		}else{//ɾ��,�޸�,�鿴
			menu.findItem(BaseField.EDIT).setVisible(true);
			menu.findItem(BaseField.DELETE).setVisible(true);
			menu.findItem(BaseField.VIEW).setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);
    } 
    /*�ڹرղ˵�ʱ����*/
	@Override
	public void onOptionsMenuClosed(Menu menu){
		Log.i(TAG, "onOptionsMenuClosed");
		isAdd=true;
	}
	/*�ڵ����˵�ѡ��ʱ����*/
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
