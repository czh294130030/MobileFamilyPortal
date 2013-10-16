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
	    amountTextView=(TextView)findViewById(R.id.amountTextView);
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
				}else{//���е�consume type������
					BaseMethod.showInformation(DailyConsumeActivityOP.this, R.string.warm_prompt, R.string.all_types_are_used);
				}
			}
		});
	}
	/* �Զ���consume�ؼ���amountEditTextʧȥ���㴥���¼�
	 * ���������ܶ�
	 * */
	private IMyAmount iMyAmount=new IMyAmount() {
		
		@Override
		public void onMyAmount(EditText editText) {
			adjustAmount(editText);
			amountTextView.setText(String.valueOf(getTotalAmount()));
		}
	};
	/*�����û������amount*/
	private void adjustAmount(EditText editText){
		double amount=0;
		try {
			amount=Double.parseDouble(editText.getText().toString().trim());
		} catch (Exception e) {
			amount=0;
		}
		editText.setText(String.valueOf(amount));
	}
	/* �����Զ���consume�ؼ���ɾ��button�����¼�
	 * �����Զ���consume�ؼ�ID���Զ���consume�ؼ�ɾ��
	 * ���������ܶ�
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
	/* �����Զ���consume�ؼ���spinner�����¼�
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
	/*��ȡ�����ܶ�*/
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
