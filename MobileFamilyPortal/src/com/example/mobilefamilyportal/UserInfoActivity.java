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

	/*判断是否是第一次触发长按userInfoListView中的Item*/
	private boolean isFirstTrigger=true;  
	/*用来控制菜单来是添加还是修改、删除，初始化默认添加菜单*/
	private boolean isAdd=true;
	private ListView userInfoListView=null;
	private SimpleCursorAdapter adapter=null;
	private EditText searchEditText=null;
	private Cursor cursor=null;
	private String TAG="Menu";
	/*存放修改或删除UserInfo的编号*/
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
	    		/*显示修改、删除菜单*/
	    		isAdd=false;
	    		/*获取操作UserInfo的编号*/
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
	
	/*绑定数据*/
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
	/*调用invalidateOptionsMenu会重新触发onCreateOptionsMenu和onPrepareOptionsMenu方法*/  
    @SuppressLint("NewApi")  
    public void menuRefresh() {  
        if (Build.VERSION.SDK_INT >= 11) {//手机或者Emulator的sdk版本  
            invalidateOptionsMenu();  
        }  
    } 
	/* 1. 当手机(Emulator)sdk版本>=11（如我的手机Android Version是4.1.1, Build.VERSION.SDK_INT是16） 
     * 在创建Activity时触发。 
     * 2. 当手机(Emulator)sdk版本<11(如我的手机Android Version是2.3.4，Build.VERSION.SDK_INT是10) 
     * 在第一次单击Menu时触发。 
     */ 
	@Override
    public boolean onCreateOptionsMenu(Menu menu) { 
		Log.i(TAG, "onCreateOptionsMenu");
		BaseMethod.setIconEnable(menu, true);//解决android 4.0显示图片
		menu.add(0, BaseField.ADD, 0, R.string.add_item).setIcon(R.drawable.menu_add);
		menu.add(0, BaseField.EDIT, 1, R.string.edit_item).setIcon(R.drawable.menu_edit);
		menu.add(0, BaseField.DELETE, 2, R.string.delete_item).setIcon(R.drawable.menu_delete);
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
		if(isAdd){//添加
			menu.findItem(BaseField.ADD).setVisible(true);
		}else{//删除和修改
			menu.findItem(BaseField.EDIT).setVisible(true);
			menu.findItem(BaseField.DELETE).setVisible(true);
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
	
	/*根据id删除用户信息*/
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
	/*当B Activity finish时触发获取resultCode和回传参数*/
    @Override    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){ 
    	/*添加用户信息成功*/
    	if(requestCode==BaseField.ADD_USERINFO&&resultCode==BaseField.ADD_SUCCESSFULLY){
    		bind();
    	}
    	/*修改用户信息成功*/
    	if(requestCode==BaseField.EDIT_USERINFO&&resultCode==BaseField.UPDATE_SUCCESSFULLY){
    		bind();
    	}
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
