package com.example.mobilefamilyportal;

import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.dal.BankCardDAL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class BankCardActivity extends Activity {

	/*�ж��Ƿ��ǵ�һ�δ�������userInfoListView�е�Item*/
	private boolean isFirstTrigger=true; 
	/*����޸Ļ�ɾ��BankCard�ı��*/
	private int id=0;
	private Cursor cursor=null;
	private SimpleCursorAdapter adapter=null;
	private ListView bankcardListView=null;
	private String TAG="MENU_BANK_CARD";
	private boolean isAdd=true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_bankcard);
	    bankcardListView=(ListView)findViewById(R.id.bankcardListView);
	    /*����ListView item�����¼�*/
	    bankcardListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
	    	public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long arg3) {
	    		/*��ȡ����BankCard�ı��*/
	    		id=Integer.parseInt(String.valueOf(parent.getItemIdAtPosition(position)));
	    		if(id!=1){
		    		/*��ʾ�޸ġ�ɾ���˵�*/
		    		isAdd=false;
		    		if (Build.VERSION.SDK_INT >= 11&&isFirstTrigger) { 
		    			menuRefresh();
		    			isFirstTrigger=false;
		    		}
		    		openOptionsMenu();
	    		}
	    		return true;
	    	}
		});
	    bind();
	}
	
	/*�����п���Ϣ*/
	@SuppressWarnings("deprecation")
	private void bind(){
		BankCardDAL bankCardDAL=new BankCardDAL(this);
		cursor=bankCardDAL.query("");
		adapter=new SimpleCursorAdapter(
				this,
				R.layout.list_bankcard,
				cursor,
				new String[]{"cardNO","bankID"},
				new int[]{R.id.cardnoTextView,R.id.cardtypeTextView});
		bankcardListView.setAdapter(adapter);
		bankCardDAL.close();
	}
	/*����invalidateOptionsMenu�����´���onCreateOptionsMenu��onPrepareOptionsMenu����*/  
    @SuppressLint("NewApi")  
    public void menuRefresh() {  
        if (Build.VERSION.SDK_INT >= 11) {//�ֻ�����Emulator��sdk�汾  
            invalidateOptionsMenu();  
        }  
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
		if(isAdd){//���
			menu.findItem(BaseField.ADD).setVisible(true);
		}else{//ɾ�����޸�
			menu.findItem(BaseField.EDIT).setVisible(true);
			menu.findItem(BaseField.DELETE).setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);
    } 
    /*�ڹرղ˵�ʱ����*/
	@Override
	public void onOptionsMenuClosed(Menu menu){
		Log.i(TAG, "onOptionsMenuClosed");
		isAdd=true;
	}
	/*ѡ��˵������¼�*/  
    public boolean onOptionsItemSelected(MenuItem item){
    	Log.i(TAG, "onOptionsItemSelected");
    	switch (item.getItemId()) {
		case BaseField.ADD:{
			Intent intent=new Intent(BankCardActivity.this,BankCardActivityOP.class);
			intent.putExtra("op", BaseField.ADD);
			startActivityForResult(intent, BaseField.ADD_BANK_CARD);
			break;}
		case BaseField.EDIT:
			break;
		case BaseField.DELETE:
			break;
		default:
			break;
		}
    	return true;
    }
    /*��B Activity finishʱ������ȡresultCode�ͻش�����*/
    @Override    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){ 
    	/*������п��ɹ�*/
    	if(requestCode==BaseField.ADD_BANK_CARD&&resultCode==BaseField.ADD_SUCCESSFULLY){
    		bind();
    	}
    	/*�޸����п��ɹ�*/
    	if(requestCode==BaseField.EDIT_BANK_CARD&&resultCode==BaseField.UPDATE_SUCCESSFULLY){
    		bind();
    	}
    }
}
