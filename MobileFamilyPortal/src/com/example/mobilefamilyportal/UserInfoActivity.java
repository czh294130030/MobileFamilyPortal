package com.example.mobilefamilyportal;

import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.dal.UserInfoDAL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class UserInfoActivity extends Activity {

	/*�ж��Ƿ��ǵ�һ�δ�������userInfoListView�е�Item*/
	private boolean isFirstTrigger=true;  
	/*�������Ʋ˵�������ӻ����޸ġ�ɾ������ʼ��Ĭ����Ӳ˵�*/
	private boolean isAdd=true;
	private ListView userInfoListView=null;
	private SimpleCursorAdapter adapter=null;
	private EditText searchEditText=null;
	private Cursor cursor=null;
	private String TAG="Menu";
	/*����޸Ļ�ɾ��UserInfo�ı��*/
	private int id=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_userinfo);
	    searchEditText=(EditText)findViewById(R.id.searchEditText);
	   searchEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				bind();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	    userInfoListView=(ListView)findViewById(R.id.userInfoListView);
	    userInfoListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
	    	public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long arg3) {
	    		/*��ʾ�޸ġ�ɾ���˵�*/
	    		isAdd=false;
	    		/*��ȡ����UserInfo�ı��*/
	    		id=Integer.parseInt(String.valueOf(parent.getItemIdAtPosition(position)));
	    		if (Build.VERSION.SDK_INT >= 11&&isFirstTrigger) { 
	    			menuRefresh();
	    			isFirstTrigger=false;
	    		}
	    		openOptionsMenu();
	    		return true;
	    	}
		});
	    bind();
	}
	
	/*������*/
	@SuppressWarnings("deprecation")
	private void bind(){
		String searchString=searchEditText.getText().toString().trim();
		String whereString="";
		if(!searchString.equals("")){
			whereString="where account like'%"+searchString+"%' or userName like '%"+searchString+"%'";
		}
		UserInfoDAL userInfoDAL=new UserInfoDAL(this);
		cursor=userInfoDAL.query(whereString);
    	adapter=new SimpleCursorAdapter(  
                this,   
                R.layout.list_userinfo,  
                cursor,   
                new String[]{"account", "userName"},  
                new int[]{R.id.accountTextView, R.id.userNameTextView});  
	    userInfoListView.setAdapter(adapter);
	    userInfoDAL.close();
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
	@Override
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
	/*�ڵ����˵�ѡ��ʱ����*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Log.i(TAG, "onOptionsItemSelected");
		switch (item.getItemId()) {
		case BaseField.ADD:{
			Intent intent=new Intent(UserInfoActivity.this, UserInfoActivityOP.class);
			Bundle bundle=new Bundle();
    		bundle.putInt("op", BaseField.ADD);
    		intent.putExtras(bundle);
			startActivityForResult(intent, BaseField.ADD_USERINFO);
			break;}
		case BaseField.EDIT:{
    		Intent intent=new Intent(UserInfoActivity.this, UserInfoActivityOP.class);
    		Bundle bundle=new Bundle();
    		bundle.putInt("op", BaseField.EDIT);
    		bundle.putInt("id", id);
    		intent.putExtras(bundle);
    		startActivityForResult(intent, BaseField.EDIT_USERINFO);
			break;}
		case BaseField.DELETE:
			deleteUserInfo(id);
			break;
		default:
			break;
		}
		return true;
	}
	
	/*����idɾ���û���Ϣ*/
	private void deleteUserInfo(int _id){
		final int userID=_id;
		new AlertDialog.Builder(this)
		.setTitle(R.string.warm_prompt)
		.setIcon(R.drawable.alert_info)
		.setMessage(R.string.confirm_to_delete)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				UserInfoDAL userInfoDAL=new UserInfoDAL(UserInfoActivity.this);
				int result=userInfoDAL.delete(userID);
				userInfoDAL.close();
				if(result>0){
					bind();
				}else{
					BaseMethod.showInformation(UserInfoActivity.this, R.string.warm_prompt, R.string.delete_unsuccessfully);
				}
			}
		}).setNegativeButton(R.string.cancel, null)
		.show();
	}
	/*��B Activity finishʱ������ȡresultCode�ͻش�����*/
    @Override    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){ 
    	/*����û���Ϣ�ɹ�*/
    	if(requestCode==BaseField.ADD_USERINFO&&resultCode==BaseField.ADD_SUCCESSFULLY){
    		bind();
    	}
    	/*�޸��û���Ϣ�ɹ�*/
    	if(requestCode==BaseField.EDIT_USERINFO&&resultCode==BaseField.UPDATE_SUCCESSFULLY){
    		bind();
    	}
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
