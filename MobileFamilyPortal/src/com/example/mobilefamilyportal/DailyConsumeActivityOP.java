package com.example.mobilefamilyportal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.dal.DailyConsumeDAL;
import com.example.dal.ParaDetailDAL;
import com.example.model.Consume;
import com.example.model.DailyConsume;
import com.example.model.KeyValue;
import com.example.mycontrol.MyConsumeControl;
import com.example.mycontrol.MyConsumeControl.IMyAmount;
import com.example.mycontrol.MyConsumeControl.IMyDelete;
import com.example.mycontrol.MyConsumeControl.IMyTypeTouchDown;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DailyConsumeActivityOP extends Activity {

	private int op;
	/*绑定所有的消费类型*/
	private List<KeyValue> items=null;
	private TextView amountTextView=null;
	private EditText dateEditText=null;
	private ImageButton calendarImageButton=null;
	private ImageButton addImageButton=null;
	private LinearLayout consumeLinearLayout=null;
	private int mYear;
	private int mMonth;
	private int mDay;
	/*用来存放日常消费编号*/
	private int id=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_dailyconsumeop);
	    calendarImageButton=(ImageButton)findViewById(R.id.calendarImageButton);
	    calendarImageButton.setOnTouchListener(onMyOnTouchListener);
	    addImageButton=(ImageButton)findViewById(R.id.addImageButton);
	    addImageButton.setOnTouchListener(onMyOnTouchListener);
	    amountTextView=(TextView)findViewById(R.id.amountTextView);
	    dateEditText=(EditText)findViewById(R.id.dateEditText);
	    consumeLinearLayout=(LinearLayout)findViewById(R.id.consumeLinearLayout);
	    /*获取消费类型*/
	    ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
	    items=paraDetailDAL.queryKeyValueList("WHERE infoID="+BaseField.CONSUME_TYPE);
	    paraDetailDAL.close();
	    /*获取操作*/
	    Bundle bundle=this.getIntent().getExtras();
	    op=bundle.getInt("op");
	    if(op==BaseField.ADD){//添加日常消费，获取当前日期
	    	Calendar calendar=Calendar.getInstance();
			mYear=calendar.get(Calendar.YEAR);
			mMonth=calendar.get(Calendar.MONTH);
			mDay=calendar.get(Calendar.DAY_OF_MONTH);
			addConsumeControl(false, null);//页面加载添加自定义消费控件
	    }else{//修改日常消费，获取修改的日期
	    	id=bundle.getInt("id");
	    	DailyConsume model=getDailyConsume(id, true);
	    	if(model!=null){
		    	String date=model.getDate();
		    	String[] dates=date.split("-");
		    	mYear=Integer.parseInt(dates[0]);
		    	mMonth=Integer.parseInt(dates[1])-1;
		    	mDay=Integer.parseInt(dates[2]);
		    	bindConsumeControls(model.getConsumeList());
		    	amountTextView.setText(String.valueOf(model.getAmount()));
	    	}
	    }
	    //页面加载填充日期显示
		dateEditText.setText(BaseMethod.getCurrentDate(mYear, mMonth, mDay));
	    calendarImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(DailyConsumeActivityOP.this, new DatePickerDialog.OnDateSetListener() {  
					@Override  
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  
						dateEditText.setText(BaseMethod.getCurrentDate(year, monthOfYear, dayOfMonth));  
						mYear=year;
						mMonth=monthOfYear;
						mDay=dayOfMonth;
					}  
				}, mYear, mMonth, mDay).show();  
			}
		});
	    /*单击添加ImageButton添加自定义消费控件*/
	    addImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(consumeLinearLayout.getChildCount()<items.size()){
					addConsumeControl(true, null);
				}else{//所有的consume type被用完
					BaseMethod.showInformation(DailyConsumeActivityOP.this, R.string.warm_prompt, R.string.all_types_are_used);
				}
			}
		});
	}
	/*在修改时绑定ConsumeControls控件*/
	private void bindConsumeControls(List<Consume> list){
		if(list.size()>0){
			for(int i=0; i<list.size(); i++){
				if(i==0){//第一个ConsumeControl无法删除
					addConsumeControl(false, list.get(i));
				}else{//其余的ConsumeControl可以删除
					addConsumeControl(true, list.get(i));
				}
			}
		}
	}
	/*添加自定义消费控件*/
	private void addConsumeControl(boolean canDelete, Consume consume){
		int lid=BaseMethod.convertCalendarToInt(Calendar.getInstance());
		MyConsumeControl myConsumeControl=new MyConsumeControl(DailyConsumeActivityOP.this, null, getTheUnusedConsumeType(items), canDelete, lid, consume);
		myConsumeControl.setId(lid);
		myConsumeControl.setOnMyTypeTouchDownListener(iMyTypeTouchDown);
		myConsumeControl.setOnMyDeleteListener(iMyDelete);
		myConsumeControl.setOnMyAmountListener(iMyAmount);
		consumeLinearLayout.addView(myConsumeControl);
	}
	/* 自定义consume控件的amountEditText失去焦点触发事件
	 * 计算消费总额
	 * */
	private IMyAmount iMyAmount=new IMyAmount() {
		
		@Override
		public void onMyAmount(EditText editText) {
			adjustAmount(editText);
			amountTextView.setText(String.valueOf(getTotalAmount()));
		}
	};
	/*调整用户输入的amount*/
	private void adjustAmount(EditText editText){
		String amountString=editText.getText().toString().trim();
		if(!amountString.equals("")){
			double amount=0;
			try {
				amount=Double.parseDouble(amountString);
			} catch (Exception e) {
				amount=0;
			}
			editText.setText(String.valueOf(amount));
		}
	}
	/* 按下自定义consume控件的删除button触发事件
	 * 根据自定义consume控件ID将自定义consume控件删除
	 * 计算消费总额
	 * */
	private IMyDelete iMyDelete=new IMyDelete() {
		@Override
		public void onMyDelete(int id, String type) {
			final int controlID=id;
			String message=getResources().getString(R.string.confirm_to_delete_consume);
			message=String.format(message, type);
			new AlertDialog.Builder(DailyConsumeActivityOP.this)
			.setTitle(R.string.warm_prompt)
			.setMessage(message)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					if(consumeLinearLayout.getChildCount()>0){
						for(int i=0; i<consumeLinearLayout.getChildCount(); i++){
							View view=consumeLinearLayout.getChildAt(i); 
							if (view instanceof MyConsumeControl) {
								MyConsumeControl myConsumeControl = (MyConsumeControl) view;
								if(myConsumeControl.getId()==controlID){
									consumeLinearLayout.removeView(myConsumeControl);
									amountTextView.setText(String.valueOf(getTotalAmount()));
								}
							}
						}
					}
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.show();
		}
	};
	/* 按下自定义consume控件的spinner触发事件
	 * 显示没有使用的消费类型和自身消费类型
	 * */
	private IMyTypeTouchDown iMyTypeTouchDown=new IMyTypeTouchDown() {
		@Override
		public void onMyTypeTouchDown(View view, ArrayAdapter<KeyValue> adapter) {
			Spinner spinner1=(Spinner)view;
			/*选中的消费类型*/
			KeyValue selectedItem=(KeyValue)spinner1.getSelectedItem();
			/*没有使用的消费类型*/
			List<KeyValue> unusedItem=getTheUnusedConsumeType(items);
			unusedItem.add(selectedItem);
			/*根据Key升序*/
			Collections.sort(unusedItem);
			/*清空现有的消费类型*/
			adapter.clear();
			/* 添加未使用的消费类型
			 * 设置spinner选中项
			 * */
			int position=0;
			for (int i=0; i<unusedItem.size(); i++) {
				if(unusedItem.get(i)==selectedItem){
					position=i;
				}
				adapter.add(unusedItem.get(i));
			}
			spinner1.setSelection(position);
		}
	};
	/*获取消费总额*/
	private double getTotalAmount(){
		double totalAmount=0;
		if(consumeLinearLayout.getChildCount()>0){
			for (int i = 0; i < consumeLinearLayout.getChildCount(); i++) {
				View view=consumeLinearLayout.getChildAt(i);
				if(view instanceof MyConsumeControl){
					MyConsumeControl myConsumeControl=(MyConsumeControl)view;
					double amount=0;
					if(!myConsumeControl.amountEditText.getText().toString().trim().equals("")){
						amount=Double.parseDouble(myConsumeControl.amountEditText.getText().toString().trim());
					}
					totalAmount+=amount;
				}
			}
		}
		return totalAmount;
	}
	/*获取没有使用的消费类型*/
	private List<KeyValue> getTheUnusedConsumeType(List<KeyValue> items){
		/*复制消费类型集合*/
		List<KeyValue> unusedItems=new ArrayList<KeyValue>();
		if(items.size()>0){
			for (KeyValue keyValue : items) {
				unusedItems.add(keyValue);
			}
		}
		/*删除已使用的消费类型*/
		if(consumeLinearLayout.getChildCount()>0){
			for(int i=0; i<consumeLinearLayout.getChildCount(); i++){
				View view=consumeLinearLayout.getChildAt(i); 
				if (view instanceof MyConsumeControl) {
					MyConsumeControl myConsumeControl = (MyConsumeControl) view;
					for (KeyValue keyValue : unusedItems) {
						if(keyValue.getKey()==((KeyValue)myConsumeControl.typeSpinner.getSelectedItem()).getKey()){
							unusedItems.remove(keyValue);
							break;
						}
					}
				}
			}
		}
		return unusedItems;
	}
	/*按下、松开ImageButton修改ImageButton背景颜色*/
	private OnTouchListener onMyOnTouchListener=new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){//按钮被按下
				v.setBackgroundColor(getResources().getColor(R.color.button_mouseover_color));
			}else if(event.getAction()==MotionEvent.ACTION_UP){//按钮被松开
				v.setBackgroundColor(getResources().getColor(R.color.transparent));
			}
			return false;
		}
	};
	
	/*添加菜单*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		BaseMethod.setIconEnable(menu, true);//解决android 4.0显示图片
		menu.add(0, BaseField.OK, 0, R.string.ok).setIcon(R.drawable.menu_ok);
		menu.add(0, BaseField.CANCEL, 0, R.string.cancel).setIcon(R.drawable.menu_cancel);
		return super.onCreateOptionsMenu(menu);
	}
	/*菜单触发事件*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case BaseField.OK:
			if(op==BaseField.ADD){//添加日常消费
				addDailyConsume();
			}else{//修改日常消费
				updateDailyConsume(id);
			}
			break;
		case BaseField.CANCEL:
			unsaveChanges();
			break;
		default:
			break;
		}
		return true;
	}
	/*添加日常消费*/
	private void addDailyConsume(){
		DailyConsume model=getDailyConsumeFromInput();
		if(model!=null){//用户添加的数据无误
			if(getDailyConsume(model.getDate())==null){//选择的日期未添加日常消费
				DailyConsumeDAL dailyConsumeDAL=new DailyConsumeDAL(DailyConsumeActivityOP.this);
				boolean flag=dailyConsumeDAL.add(model);
				dailyConsumeDAL.close();
				if(flag){
					Intent intent=new Intent();
					setResult(BaseField.ADD_SUCCESSFULLY, intent);
					this.finish();
				}else{
					BaseMethod.showInformation(DailyConsumeActivityOP.this, R.string.warm_prompt, R.string.add_unsuccessfully);
				}
			}else{
				BaseMethod.showInformation(DailyConsumeActivityOP.this, R.string.warm_prompt, R.string.daily_consume_exsit);
			}
		}
	}
	/*修改日常消费*/
	private void updateDailyConsume(int _id){
		DailyConsume model=getDailyConsumeFromInput();
		if(model!=null){//用户添加数据无误
			DailyConsume item=getDailyConsume(_id, false);
			if(!model.getDate().equals(item.getDate())){//用户在修改日常消费时修改了日期
				if(getDailyConsume(model.getDate())!=null){//选择的日期已添加日常消费
					BaseMethod.showInformation(DailyConsumeActivityOP.this, R.string.warm_prompt, R.string.daily_consume_exsit);
					return;
				}
			}
			model.setDailyID(_id);
			DailyConsumeDAL dailyConsumeDAL=new DailyConsumeDAL(DailyConsumeActivityOP.this);
			boolean flag=dailyConsumeDAL.update(model);
			dailyConsumeDAL.close();
			if(flag){
				Intent intent=new Intent();
				setResult(BaseField.UPDATE_SUCCESSFULLY, intent);
				this.finish();
			}else{
				BaseMethod.showInformation(DailyConsumeActivityOP.this, R.string.warm_prompt, R.string.add_unsuccessfully);
			}
		}
	}
	/*根据日常消费编号获取日常消费Info&Details*/
	private DailyConsume getDailyConsume(int id, boolean isNeedDetails){
		DailyConsume item=new DailyConsume();
		item.setDailyID(id);
		DailyConsumeDAL dailyConsumeDAL=new DailyConsumeDAL(DailyConsumeActivityOP.this);
		DailyConsume model=dailyConsumeDAL.queryModel(item, isNeedDetails);
		dailyConsumeDAL.close();
		return model;
	}
	/*根据日期获取日常消费Info*/
	private DailyConsume getDailyConsume(String date){
		DailyConsume item=new DailyConsume();
		item.setDate(date);
		DailyConsumeDAL dailyConsumeDAL=new DailyConsumeDAL(DailyConsumeActivityOP.this);
		DailyConsume model=dailyConsumeDAL.queryModel(item, false);
		dailyConsumeDAL.close();
		return model;
	}
	
	/*获取用户输入的日常消费*/
	private DailyConsume getDailyConsumeFromInput(){
		if(validateDailyConsume()){
			List<Consume> consumeList=new ArrayList<Consume>();
			if(consumeLinearLayout.getChildCount()>0){
				for(int i=0; i<consumeLinearLayout.getChildCount(); i++){
					View view =consumeLinearLayout.getChildAt(i);
					if(view instanceof MyConsumeControl){
						Consume consumeItem=new Consume();
						MyConsumeControl myConsumeControl=(MyConsumeControl) view;
						consumeItem.setAmount(Double.parseDouble(myConsumeControl.amountEditText.getText().toString().trim()));
						consumeItem.setDescription(myConsumeControl.descriptionEditText.getText().toString().trim());
						consumeItem.setTypeID(((KeyValue)myConsumeControl.typeSpinner.getSelectedItem()).getKey());
						consumeList.add(consumeItem);
					}
				}
			}
			DailyConsume item=new DailyConsume();
			item.setAmount(Double.parseDouble(amountTextView.getText().toString().trim()));
			item.setDate(dateEditText.getText().toString().trim());
			item.setConsumeList(consumeList);
			return item;
		}else{
			return null;
		}
	}
	/*判断用户输入*/
	private boolean validateDailyConsume(){
		if(consumeLinearLayout.getChildCount()>0){
			for(int i=0; i<consumeLinearLayout.getChildCount(); i++){
				View view=consumeLinearLayout.getChildAt(i); 
				if (view instanceof MyConsumeControl) {
					MyConsumeControl myConsumeControl = (MyConsumeControl) view;
					String amount=myConsumeControl.amountEditText.getText().toString().trim();
					if(amount.equals("")){//未输入消费金额
						BaseMethod.showInformation(DailyConsumeActivityOP.this, R.string.warm_prompt, R.string.amount_required);
						myConsumeControl.amountEditText.requestFocus();
						return false;
					}
					String description=myConsumeControl.descriptionEditText.getText().toString().trim();
					if(description.equals("")){//未输入消费描述
						BaseMethod.showInformation(DailyConsumeActivityOP.this, R.string.warm_prompt, R.string.description_required);
						myConsumeControl.descriptionEditText.requestFocus();
						return false;
					}
				}
			}
		}
		return true;
	}
	/*关闭Activity*/ 
	@Override   
    public boolean onKeyDown(int keyCode, KeyEvent event){  
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	unsaveChanges();
        }
        if(keyCode==KeyEvent.KEYCODE_MENU){
        	dateEditText.requestFocus();
        }
        return super.onKeyDown(keyCode, event);  
    }
	/*不保存跳转到用户信息页面*/
	private void unsaveChanges(){
		new AlertDialog.Builder(this)
    	.setTitle(R.string.warm_prompt)
    	.setIcon(R.drawable.alert_info)
    	.setMessage(R.string.unsave_changes)
    	.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent cancelIntent=new Intent();
				setResult(BaseField.ADD_CANCEL, cancelIntent);
	            finish();  
			}
		}).setNegativeButton(R.string.cancel, null)
		.show();
	}
}
