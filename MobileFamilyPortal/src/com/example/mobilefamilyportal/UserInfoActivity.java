package com.example.mobilefamilyportal;

import com.example.base.BaseField;
import com.example.dal.UserInfoDAL;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class UserInfoActivity extends Activity {

	/*用来控制菜单来是添加还是修改、删除，初始化默认添加菜单*/
	private boolean isAdd=true;
	private ListView userInfoListView=null;
	private SimpleCursorAdapter adapter=null;
	private Cursor cursor=null;
	/*存放修改或删除UserInfo的编号*/
	private int id=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_userinfo);
	    userInfoListView=(ListView)findViewById(R.id.userInfoListView);
	    userInfoListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
	    	public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long arg3) {
	    		/*获取操作UserInfo的编号*/
	    		id=Integer.parseInt(String.valueOf(parent.getItemIdAtPosition(position)));
	    		/*显示修改、删除菜单*/
	    		isAdd=false;
	    		openOptionsMenu();
	    		return true;
	    	}
		});
	    bind();
	}
	
	/*绑定数据*/
	@SuppressWarnings("deprecation")
	private void bind(){
		UserInfoDAL userInfoDAL=new UserInfoDAL(this);
		cursor=userInfoDAL.query();
    	adapter=new SimpleCursorAdapter(  
                this,   
                R.layout.list_userinfo,  
                cursor,   
                new String[]{"_id", "account", "userName"},  
                new int[]{R.id.userIDTextView, R.id.accountTextView, R.id.userNameTextView});  
	    userInfoListView.setAdapter(adapter);
	    userInfoDAL.close();
	}
	
	/*进入Launcher后第一次点MENU按钮时触发*/  
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {  
        // Inflate the menu; this adds items to the action bar if it is present.  
        getMenuInflater().inflate(R.menu.main, menu);  
        return true;  
    } 
	/*在打开菜单是触发*/
	@Override  
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if(isAdd){
			menu.add(0, BaseField.ADD, 0, R.string.add_item);
		}else{
			menu.add(0, BaseField.EDIT, 0, R.string.edit_item);
			menu.add(0, BaseField.DELETE, 1, R.string.delete_item);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	/*在关闭菜单时出发*/
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		isAdd=true;
	}
	/*在单击菜单选项时触发*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
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
		default:
			break;
		}
		return true;
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
    
    /*退出程序*/ 
	@Override   
    public boolean onKeyDown(int keyCode, KeyEvent event){  
        if(keyCode==KeyEvent.KEYCODE_BACK){  
            this.finish();  
        }  
        return super.onKeyDown(keyCode, event);  
    }
}
