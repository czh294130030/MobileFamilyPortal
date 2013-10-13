package com.example.mycontrol;

import java.util.List;

import com.example.mobilefamilyportal.R;
import com.example.model.KeyValue;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class MyConsumeControl extends LinearLayout{
	/*定义接口*/
	public interface IMyTypeTouchDown{
		public void onMyTypeTouchDown(View view, ArrayAdapter<KeyValue> adapter);
	}
	/*初始化接口变量*/
	IMyTypeTouchDown iMyTypeTouchDown=null;
	/*自定义事件*/
	public void setOnMyTypeTouchDownListener(IMyTypeTouchDown _iMyTypeTouchDown){
		iMyTypeTouchDown=_iMyTypeTouchDown;
	}
	private Spinner typeSpinner=null;
	private ArrayAdapter<KeyValue> adapter=null;
	private EditText amountEditText=null;
	private EditText descriptionEditText=null;
	public int typeID=0;
	public MyConsumeControl(Context context){
		super(context);
	}
	public MyConsumeControl(Context context, AttributeSet attr, List<KeyValue> items, boolean canDelete){
		super(context, attr);
		/*导入布局界面*/
		LayoutInflater layoutInflater=
				(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.control_consume, this);
		amountEditText=(EditText)findViewById(R.id.amountEditText);
		descriptionEditText=(EditText)findViewById(R.id.descriptionEditText);
		typeSpinner=(Spinner)findViewById(R.id.typeSpinner);
		/*按下Spinner重置items*/
		typeSpinner.setOnTouchListener(new Spinner.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					iMyTypeTouchDown.onMyTypeTouchDown(view, adapter);
				}
				return false;
			}
		});
		/*选择消费类型*/
		typeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ 
				typeID=((KeyValue)parent.getSelectedItem()).getKey();		
	    	}
	    	@Override  
	    	public void onNothingSelected(AdapterView<?> arg0) {  
	    	} 
		});
		bind(context, items);
	}
	/*绑定加载数据*/
	private void bind(Context context, List<KeyValue> items){
		
		bindConsumeType(context, items);
	}
	/*绑定消费类型*/
	private void bindConsumeType(Context context, List<KeyValue> items){
		adapter=new ArrayAdapter<KeyValue>(
				context, 
				android.R.layout.simple_spinner_item, 
				items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		typeSpinner.setAdapter(adapter);
	}
}
