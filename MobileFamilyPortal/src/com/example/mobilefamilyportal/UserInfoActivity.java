package com.example.mobilefamilyportal;

import com.example.base.BaseField;
import com.example.dal.UserInfoDAL;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class UserInfoActivity extends Activity {

	private ListView userInfoListView=null;
	private SimpleCursorAdapter adapter=null;
	private Cursor cursor=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_userinfo);
	    userInfoListView=(ListView)findViewById(R.id.userInfoListView);
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
	
	/*添加菜单*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0, BaseField.ADD, 0, R.string.add_item);
		menu.add(0, BaseField.EDIT, 0, R.string.edit_item);
		menu.add(0, BaseField.DELETE, 0, R.string.delete_item);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	/*选择菜单触发事件*/
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case BaseField.ADD:
			Intent intent=new Intent(UserInfoActivity.this, UserInfoActivityOP.class);
			startActivityForResult(intent, BaseField.ADD_USERINFO);
			break;
		case BaseField.EDIT:
			break;
		case BaseField.DELETE:
			break;
		default:
			break;
		}
		return true;
	}
	
    @Override    
    /*当B Activity finish时触发获取resultCode和回传参数*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data){ 
    	/*添加用户信息成功*/
    	if(requestCode==BaseField.ADD_USERINFO&&resultCode==BaseField.ADD_SUCCESSFULLY){
    		bind();
    	}
    }
    
	@Override  
    /*退出程序*/  
    public boolean onKeyDown(int keyCode, KeyEvent event){  
        if(keyCode==KeyEvent.KEYCODE_BACK){  
            this.finish();  
        }  
        return super.onKeyDown(keyCode, event);  
    }
}
