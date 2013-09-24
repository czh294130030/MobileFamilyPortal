package com.example.mobilefamilyportal;

import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.dal.BankCardDAL;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class BankCardActivity extends Activity {

	/*判断是否是第一次触发长按userInfoListView中的Item*/
	private boolean isFirstTrigger=true; 
	/*存放修改或删除BankCard的编号*/
	private int id=0;
	private Cursor cursor=null;
	private SimpleCursorAdapter adapter=null;
	private ListView bankcardListView=null;
	private String TAG="MENU_BANK_CARD";
	private boolean isAdd=true;
	private EditText searchEditText=null;
	private ImageButton clearImageButton=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_bankcard);
	    /*清空搜索条件*/
	    clearImageButton=(ImageButton)findViewById(R.id.clearImageButton);
	    clearImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				searchEditText.setText("");
			}
		});
	    /*修改EditText时重新绑定数据和显示（隐藏）清空按钮*/
	    searchEditText=(EditText)findViewById(R.id.searchEditText);
	    searchEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				bind();
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			@Override
			public void afterTextChanged(Editable arg0) {
				String searchString=searchEditText.getText().toString().trim();
				if(searchString.equals("")){
					clearImageButton.setVisibility(View.INVISIBLE);
				}else{
					clearImageButton.setVisibility(View.VISIBLE);
				}
			}
		});
	    bankcardListView=(ListView)findViewById(R.id.bankcardListView);
	    /*长按ListView item触发事件*/
	    bankcardListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
	    	public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long arg3) {
	    		/*获取操作BankCard的编号*/
	    		id=Integer.parseInt(String.valueOf(parent.getItemIdAtPosition(position)));
	    		/*显示修改、删除菜单*/
	    		isAdd=false;
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
	
	/*绑定银行卡信息*/
	@SuppressWarnings("deprecation")
	private void bind(){
		String searchString=searchEditText.getText().toString().trim();
		String whereString="";
		if(!searchString.equals("")){
			whereString="where a.cardNO like'%"+searchString+"%' or c.description like '%"+searchString+"%'";
		}
		BankCardDAL bankCardDAL=new BankCardDAL(this);
		cursor=bankCardDAL.query(whereString);
		adapter=new SimpleCursorAdapter(
				this,
				R.layout.list_bankcard,
				cursor,
				new String[]{"cardNO","cardType"},
				new int[]{R.id.cardnoTextView,R.id.cardtypeTextView});
		bankcardListView.setAdapter(adapter);
		bankCardDAL.close();
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
	/*选择菜单触发事件*/  
    public boolean onOptionsItemSelected(MenuItem item){
    	Log.i(TAG, "onOptionsItemSelected");
    	switch (item.getItemId()) {
		case BaseField.ADD:{
			Intent intent=new Intent(BankCardActivity.this,BankCardActivityOP.class);
			intent.putExtra("op", BaseField.ADD);
			startActivityForResult(intent, BaseField.ADD_BANK_CARD);
			break;}
		case BaseField.EDIT:{
			Intent intent=new Intent(BankCardActivity.this,BankCardActivityOP.class);
			intent.putExtra("id", id);
			intent.putExtra("op", BaseField.EDIT);
			startActivityForResult(intent, BaseField.EDIT_BANK_CARD);
			break;}
		case BaseField.DELETE:
			deleteBankCard(id);
			break;
		case BaseField.VIEW:
			
			break;
		default:
			break;
		}
    	return true;
    }
    /*根据编号删除银行卡信息*/
    private void deleteBankCard(int _id){
    	final int cardID=_id;
    	new AlertDialog.Builder(this)
    	.setTitle(R.string.warm_prompt)
    	.setIcon(R.drawable.alert_info)
    	.setMessage(R.string.confirm_to_delete)
    	.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BankCardDAL bankCardDAL=new BankCardDAL(BankCardActivity.this);
				int result=bankCardDAL.delete(cardID);
				bankCardDAL.close();
				if(result>0){
					bind();
				}else {
					BaseMethod.showInformation(BankCardActivity.this, R.string.warm_prompt, R.string.delete_unsuccessfully);
				}
			}
		}).setNegativeButton(R.string.cancel, null)
		.show();
    }
    /*当B Activity finish时触发获取resultCode和回传参数*/
    @Override    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){ 
    	/*添加银行卡成功*/
    	if(requestCode==BaseField.ADD_BANK_CARD&&resultCode==BaseField.ADD_SUCCESSFULLY){
    		bind();
    	}
    	/*修改银行卡成功*/
    	if(requestCode==BaseField.EDIT_BANK_CARD&&resultCode==BaseField.UPDATE_SUCCESSFULLY){
    		bind();
    	}
    }
}
