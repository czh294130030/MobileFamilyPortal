package com.example.mobilefamilyportal;

import java.util.List;
import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.base.SoapService;
import com.example.dal.DailyConsumeDAL;
import com.example.model.DailyConsume;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

public class DailyConsumeActivity extends Activity {

	/*判断是否是第一次触发长按userInfoListView中的Item*/
	private boolean isFirstTrigger=true; 
	/*用来控制菜单来是添加还是修改、删除，初始化默认添加菜单*/
	private boolean isAdd=true;
	/*存放删除或者修改日常消费的编号*/
	private int id=0;
	private String TAG="MENU_DAILY_CONSUME";
	private EditText searchEditText=null;
	private ImageButton clearImageButton=null;
	private ListView dailyconsumeListView=null;
	private Cursor cursor=null;
	private SimpleCursorAdapter adapter=null;
	private ProgressDialog syncProgressDialog=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_dailyconsume);
	    /*长按ListView item触发事件*/
	    dailyconsumeListView=(ListView)findViewById(R.id.dailyconsumeListView);
	    dailyconsumeListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
	    	public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long arg3) {
	    		/*获取删除或者修改日常消费的编号*/
	    		id=Integer.parseInt(String.valueOf(parent.getItemIdAtPosition(position)));
	    		/*显示修改，删除，查看菜单*/
	    		isAdd=false;
	    		if (Build.VERSION.SDK_INT >= 11&&isFirstTrigger) { 
	    			menuRefresh();
	    			isFirstTrigger=false;
	    		}
	    		openOptionsMenu();
	    		return true;
	    	}
		});
	    /*清空搜索条件*/
	    clearImageButton=(ImageButton)findViewById(R.id.clearImageButton);
	    clearImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchEditText.setText("");
			}
		});
	    /*修改EditText时重新绑定数据和显示（隐藏）清空按钮*/
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
				String searchString=searchEditText.getText().toString().trim();
				if(searchString.equals("")){
					clearImageButton.setVisibility(View.INVISIBLE);
				}else{
					clearImageButton.setVisibility(View.VISIBLE);
				}
			}
		});
	    bind();
	}
	/*调用invalidateOptionsMenu会重新触发onCreateOptionsMenu和onPrepareOptionsMenu方法*/  
    @SuppressLint("NewApi")  
    public void menuRefresh() {  
        if (Build.VERSION.SDK_INT >= 11) {//手机或者Emulator的sdk版本  
            invalidateOptionsMenu();  
        }  
    }
	/*绑定银行卡信息*/
	@SuppressWarnings("deprecation")
	private void bind(){
		String searchString=searchEditText.getText().toString().trim();
		String whereString="";
		if(!searchString.equals("")){
			whereString="where date like '%"+searchString+"%'";
		}
		DailyConsumeDAL dailyConsumeDAL=new DailyConsumeDAL(DailyConsumeActivity.this);
		cursor=dailyConsumeDAL.query(whereString);
		adapter=new SimpleCursorAdapter(
				this,
				R.layout.list_dailyconsume,
				cursor,
				new String[]{"amount","date"},
				new int[]{R.id.amountTextView,R.id.dateTextView});
		dailyconsumeListView.setAdapter(adapter);
		dailyConsumeDAL.close();
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
		menu.add(0, BaseField.SYNC, 4, R.string.sync_item).setIcon(R.drawable.menu_sync);
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
		menu.findItem(BaseField.SYNC).setVisible(false);
		if(isAdd){//添加
			menu.findItem(BaseField.ADD).setVisible(true);
			menu.findItem(BaseField.SYNC).setVisible(true);
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
    		Intent intent=new Intent(DailyConsumeActivity.this, DailyConsumeActivityOP.class);
    		Bundle bundle=new Bundle();
    		bundle.putInt("op", BaseField.EDIT);
    		bundle.putInt("id", id);
    		intent.putExtras(bundle);
    		startActivityForResult(intent, BaseField.EDIT_DAILY_CONSUME);
			break;}
		case BaseField.VIEW:{
			Intent intent=new Intent(DailyConsumeActivity.this, DailyConsumeActivityView.class);
			Bundle bundle=new Bundle();
			bundle.putInt("id", id);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
		case BaseField.DELETE:
			deleteDailyConsume(id);
			break;
		case BaseField.SYNC:
			syncDailyConsume();
			break;
		default:
			break;
		}
		return true;
	}
	private Handler handler=new Handler(){    
        public void handleMessage(Message msg){    
            switch (msg.what) {    
            case 0:    
            	Toast.makeText(DailyConsumeActivity.this, R.string.sync_unsuccessfully, Toast.LENGTH_LONG).show();
                break;
            case 1:
            	Toast.makeText(DailyConsumeActivity.this, R.string.sync_successfully, Toast.LENGTH_LONG).show();
            	break;
            default:    
            	Toast.makeText(DailyConsumeActivity.this, R.string.sync_unsuccessfully, Toast.LENGTH_LONG).show();
                break;    
            }    
            super.handleMessage(msg);    
        }    
    };
	private Runnable myRunnable=new Runnable() {
		@Override
		public void run() {
			/*同步数据*/
			DailyConsumeDAL dailyConsumeDAL=new DailyConsumeDAL(DailyConsumeActivity.this);
			List<DailyConsume> list=dailyConsumeDAL.queryDailyConsumes("", true);
			dailyConsumeDAL.close();
			SoapService soapService=new SoapService();
			boolean flag=soapService.syncDailyConsume(list, BaseField.SYNCDAILYCONSUME);
			/*关闭ProgressDialog*/
			syncProgressDialog.dismiss();
			/*操作界面*/
			Message msg=new Message();
			msg.what=flag==true?1:0;
			handler.sendMessage(msg);
		}
	};
	/*和远程服务器同步dailyconsume*/
	private void syncDailyConsume(){
		syncProgressDialog=ProgressDialog.show(DailyConsumeActivity.this, BaseField.WARM_PROMPT, BaseField.SYNC_DAILYCONSUME, true);
		new Thread(myRunnable).start();
	}
	/*删除日常消费Info&Details*/
	private void deleteDailyConsume(int _id){
		final int dailyID=_id;
    	new AlertDialog.Builder(this)
    	.setTitle(R.string.warm_prompt)
    	.setIcon(R.drawable.alert_info)
    	.setMessage(R.string.confirm_to_delete_dailyconsume)
    	.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DailyConsumeDAL dailyConsumeDAL=new DailyConsumeDAL(DailyConsumeActivity.this);
				boolean result=dailyConsumeDAL.delete(dailyID);
				dailyConsumeDAL.close();
				if(result){
					bind();
				}else {
					BaseMethod.showInformation(DailyConsumeActivity.this, R.string.warm_prompt, R.string.delete_unsuccessfully);
				}
			}
		}).setNegativeButton(R.string.cancel, null)
		.show();
	}
	/*当B Activity finish时触发获取resultCode和回传参数*/
    @Override    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){ 
    	/*添加日常消费成功*/
    	if(requestCode==BaseField.ADD_DAILY_CONSUME&&resultCode==BaseField.ADD_SUCCESSFULLY){
    		bind();
    	}
    	/*修改日常消费成功*/
    	if(requestCode==BaseField.EDIT_DAILY_CONSUME&&resultCode==BaseField.UPDATE_SUCCESSFULLY){
    		bind();
    	}
    }
}
