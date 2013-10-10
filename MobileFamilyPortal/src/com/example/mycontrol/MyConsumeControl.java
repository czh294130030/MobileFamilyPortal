package com.example.mycontrol;

import java.util.List;

import com.example.mobilefamilyportal.R;
import com.example.model.KeyValue;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class MyConsumeControl extends LinearLayout{
	
	private Spinner typeSpinner=null;
	private EditText amountEditText=null;
	private EditText descriptionEditText=null;
	public MyConsumeControl(Context context){
		super(context);
	}
	public MyConsumeControl(Context context, AttributeSet attr, List<KeyValue> items){
		super(context, attr);
		/*导入布局界面*/
		LayoutInflater layoutInflater=
				(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.control_consume, this);
		
		typeSpinner=(Spinner)findViewById(R.id.typeSpinner);
		amountEditText=(EditText)findViewById(R.id.amountEditText);
		descriptionEditText=(EditText)findViewById(R.id.descriptionEditText);
		
		ArrayAdapter<KeyValue> adapter=new ArrayAdapter<KeyValue>(
				context, 
				android.R.layout.simple_spinner_item, 
				items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		typeSpinner.setAdapter(adapter);
	}
}
