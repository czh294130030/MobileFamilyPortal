package com.example.mobilefamilyportal;

import java.util.Calendar;
import java.util.List;

import com.example.base.BaseField;
import com.example.base.BaseMethod;
import com.example.dal.ParaDetailDAL;
import com.example.model.KeyValue;
import com.example.mycontrol.MyConsumeControl;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DailyConsumeActivityOP extends Activity {

	private TextView amountTextView=null;
	private EditText dateEditText=null;
	private ImageButton calendarImageButton=null;
	private ImageButton addImageButton=null;
	private LinearLayout consumeLinearLayout=null;
	private int mYear;
	private int mMonth;
	private int mDay;
	private List<KeyValue> items=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_dailyconsumeop);
	    calendarImageButton=(ImageButton)findViewById(R.id.calendarImageButton);
	    calendarImageButton.setOnTouchListener(onMyOnTouchListener);
	    addImageButton=(ImageButton)findViewById(R.id.addImageButton);
	    addImageButton.setOnTouchListener(onMyOnTouchListener);
	    amountTextView=(TextView)findViewById(R.id.amountEditText);
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
	    /*添加MyConsumeControl控件*/
	    ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
	    items=paraDetailDAL.queryKeyValueList("WHERE infoID="+BaseField.CONSUME_TYPE);
	    paraDetailDAL.close();
	    addImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MyConsumeControl myConsumeControl=new MyConsumeControl(DailyConsumeActivityOP.this, null, items);
				consumeLinearLayout.addView(myConsumeControl);
			}
		});
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
