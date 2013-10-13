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
import com.example.mycontrol.MyConsumeControl.IMyTypeTouchDown;

import android.app.Activity;
import android.app.DatePickerDialog;
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

	/*�����е���������*/
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
	    amountTextView=(TextView)findViewById(R.id.amountEditText);
	    dateEditText=(EditText)findViewById(R.id.dateEditText);
	    consumeLinearLayout=(LinearLayout)findViewById(R.id.consumeLinearLayout);
	    /* ��ȡ��ǰ����
	     * Ĭ����ʾ��ǰ����
	     * ѡ�����ڰ�ť�޸���ʾ����
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
	    /* ���MyConsumeControl�ؼ�
	     * Activity�������һ������ɾ����MyConsumeControl
	     * ������ť��ӿ�ɾ����MyConsumeControl
	     * */
	    ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
	    items=paraDetailDAL.queryKeyValueList("WHERE infoID="+BaseField.CONSUME_TYPE);
	    paraDetailDAL.close();
	    MyConsumeControl firstConsumeControl=new MyConsumeControl(DailyConsumeActivityOP.this, null, getTheUnusedConsumeType(items), false);
	    firstConsumeControl.setOnMyTypeTouchDownListener(iMyTypeTouchDown);
	    consumeLinearLayout.addView(firstConsumeControl);
	    addImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(consumeLinearLayout.getChildCount()<items.size()){
					MyConsumeControl myConsumeControl=new MyConsumeControl(DailyConsumeActivityOP.this, null, getTheUnusedConsumeType(items), true);
					myConsumeControl.setOnMyTypeTouchDownListener(iMyTypeTouchDown);
					consumeLinearLayout.addView(myConsumeControl);
				}
			}
		});
	}
	/* �����Զ���consume�ؼ������¼�
	 * ��ʾû��ʹ�õ��������ͺ�������������
	 * */
	private IMyTypeTouchDown iMyTypeTouchDown=new IMyTypeTouchDown() {
		
		@Override
		public void onMyTypeTouchDown(View view, ArrayAdapter<KeyValue> adapter) {
			Spinner spinner1=(Spinner)view;
			/*ѡ�е���������*/
			KeyValue selectedItem=(KeyValue)spinner1.getSelectedItem();
			/*û��ʹ�õ���������*/
			List<KeyValue> unusedItem=getTheUnusedConsumeType(items);
			unusedItem.add(selectedItem);
			/*����Key����*/
			Collections.sort(unusedItem);
			/*������е���������*/
			adapter.clear();
			/* ���δʹ�õ���������
			 * ����spinnerѡ����
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
	/*��ȡû��ʹ�õ���������*/
	private List<KeyValue> getTheUnusedConsumeType(List<KeyValue> items){
		/*�����������ͼ���*/
		List<KeyValue> unusedItems=new ArrayList<KeyValue>();
		if(items.size()>0){
			for (KeyValue keyValue : items) {
				unusedItems.add(keyValue);
			}
		}
		/*ɾ����ʹ�õ���������*/
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
	/*���¡��ɿ�ImageButton�޸�ImageButton������ɫ*/
	private OnTouchListener onMyOnTouchListener=new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){//��ť������
				v.setBackgroundColor(getResources().getColor(R.color.button_mouseover_color));
			}else if(event.getAction()==MotionEvent.ACTION_UP){//��ť���ɿ�
				v.setBackgroundColor(getResources().getColor(R.color.transparent));
			}
			return false;
		}
	};
}
