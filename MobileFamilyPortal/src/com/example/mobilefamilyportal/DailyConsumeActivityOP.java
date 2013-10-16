package com.example.mobilefamilyportal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.dal.ParaDetailDAL;
import com.example.model.KeyValue;
import com.example.mycontrol.MyConsumeControl;
import com.example.mycontrol.MyConsumeControl.IMyAmount;
import com.example.mycontrol.MyConsumeControl.IMyDelete;
import com.example.mycontrol.MyConsumeControl.IMyTypeTouchDown;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.CalendarContract.Instances;
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
	    /* 获取当前日期
	     * 默认显示当前日期
	     * 选择日期按钮修改显示日期
	     * */
		Calendar calendar=Calendar.getInstance();
		mYear=calendar.get(Calendar.YEAR);
		mMonth=calendar.get(Calendar.MONTH);
		mDay=calendar.get(Calendar.DAY_OF_MONTH);
		dateEditText.setText(BaseMethod.getCurrentDate(mYear, mMonth, mDay));
	    calendarImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(DailyConsumeActivityOP.this, new DatePickerDialog.OnDateSetListener() {  
					@Override  
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  
						dateEditText.setText(BaseMethod.getCurrentDate(year, monthOfYear, dayOfMonth));  
					}  
				}, mYear, mMonth, mDay).show();  
			}
		});
	    /* 添加MyConsumeControl控件
	     * Activity创建添加一个不可删除的MyConsumeControl
	     * 单击按钮添加可删除的MyConsumeControl
	     * */
	    ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
	    items=paraDetailDAL.queryKeyValueList("WHERE infoID="+BaseField.CONSUME_TYPE);
	    paraDetailDAL.close();
	    int fid=BaseMethod.convertCalendarToInt(Calendar.getInstance());
	    MyConsumeControl firstConsumeControl=new MyConsumeControl(DailyConsumeActivityOP.this, null, getTheUnusedConsumeType(items), false, fid);
	    firstConsumeControl.setId(fid);
	    firstConsumeControl.setOnMyTypeTouchDownListener(iMyTypeTouchDown);
	    firstConsumeControl.setOnMyAmountListener(iMyAmount);
	    consumeLinearLayout.addView(firstConsumeControl);
	    addImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(consumeLinearLayout.getChildCount()<items.size()){
					int lid=BaseMethod.convertCalendarToInt(Calendar.getInstance());
					MyConsumeControl myConsumeControl=new MyConsumeControl(DailyConsumeActivityOP.this, null, getTheUnusedConsumeType(items), true, lid);
					myConsumeControl.setId(lid);
					myConsumeControl.setOnMyTypeTouchDownListener(iMyTypeTouchDown);
					myConsumeControl.setOnMyDeleteListener(iMyDelete);
					myConsumeControl.setOnMyAmountListener(iMyAmount);
					consumeLinearLayout.addView(myConsumeControl);
				}else{//所有的consume type被用完
					BaseMethod.showInformation(DailyConsumeActivityOP.this, R.string.warm_prompt, R.string.all_types_are_used);
				}
			}
		});
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
		double amount=0;
		try {
			amount=Double.parseDouble(editText.getText().toString().trim());
		} catch (Exception e) {
			amount=0;
		}
		editText.setText(String.valueOf(amount));
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
						if(keyValue.getKey()==myConsumeControl.typeID){
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
}
