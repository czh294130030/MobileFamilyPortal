package com.example.mycontrol;

import java.util.List;

import com.example.mobilefamilyportal.R;
import com.example.model.Consume;
import com.example.model.KeyValue;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class MyConsumeControl extends LinearLayout{
	/*����ӿ�*/
	public interface IMyTypeTouchDown{
		public void onMyTypeTouchDown(View view, ArrayAdapter<KeyValue> adapter);
	}
	public interface IMyDelete{
		public void onMyDelete(int id, String type);
	}
	public interface IMyAmount{
		public void onMyAmount(EditText editText);
	}
	/*��ʼ���ӿڱ���*/
	IMyTypeTouchDown iMyTypeTouchDown=null;
	IMyDelete iMyDelete=null;
	IMyAmount iMyAmount=null;
	/*�Զ����¼�*/
	public void setOnMyTypeTouchDownListener(IMyTypeTouchDown _iMyTypeTouchDown){
		iMyTypeTouchDown=_iMyTypeTouchDown;
	}
	public void setOnMyDeleteListener(IMyDelete _iMyDelete){
		iMyDelete=_iMyDelete;
	}
	public void setOnMyAmountListener(IMyAmount _iMyAmount){
		iMyAmount=_iMyAmount;
	}
	public Spinner typeSpinner=null;
	private ArrayAdapter<KeyValue> adapter=null;
	public EditText amountEditText=null;
	public EditText descriptionEditText=null;
	private Button deleteButton=null;
	private int controlID=0;
	public MyConsumeControl(Context context){
		super(context);
	}
	public MyConsumeControl(Context context, AttributeSet attr, List<KeyValue> items, boolean canDelete, int id, Consume consume){
		super(context, attr);
		/*�����Զ���ؼ�ID*/
		controlID=id;
		/*���벼�ֽ���*/
		LayoutInflater layoutInflater=
				(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.control_consume, this);
		descriptionEditText=(EditText)findViewById(R.id.descriptionEditText);
		/*��amountEditTextʧȥ�������������*/
		amountEditText=(EditText)findViewById(R.id.amountEditText);
		amountEditText.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(!arg1){
					EditText editText=(EditText)arg0;
					iMyAmount.onMyAmount(editText);
				}
			}
		});
		/*ɾ��consume*/
		deleteButton=(Button)findViewById(R.id.deleteButton);
		if(canDelete){//��canDelete=trueɾ����ť��ʾ
			deleteButton.setVisibility(View.VISIBLE);
		}else{//��canDelete=falseɾ����ť����
			/*Ӱ��, ռ�ÿռ�*/
			//deleteButton.setVisibility(View.INVISIBLE);
			/*Ӱ�أ� ��ռ�ÿռ�*/
			deleteButton.setVisibility(View.GONE);
		}
		deleteButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String type=((KeyValue)typeSpinner.getSelectedItem()).getValue();
				iMyDelete.onMyDelete(controlID, type);
			}
		});
		typeSpinner=(Spinner)findViewById(R.id.typeSpinner);
		/*����Spinner����items*/
		typeSpinner.setOnTouchListener(new Spinner.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					iMyTypeTouchDown.onMyTypeTouchDown(view, adapter);
				}
				return false;
			}
		});
		bind(context, items, consume);
	}
	/*�󶨼�������*/
	private void bind(Context context, List<KeyValue> items, Consume consume){
		if(consume!=null){//�������޸�
			amountEditText.setText(String.valueOf(consume.getAmount()));
			descriptionEditText.setText(consume.getDescription());
			bindConsumeType(context, items, consume.getTypeID());
		}else{
			bindConsumeType(context, items, 0);
		}
	}
	/*����������*/
	private void bindConsumeType(Context context, List<KeyValue> items, int typeID){
		if(items.size()>0){
			int selectedPosition=0;
			if(typeID!=0){//�����޸�
				for (KeyValue item : items) {
					if(item.getKey()!=typeID){selectedPosition++;}
					else{break;}
				}
			}
			adapter=new ArrayAdapter<KeyValue>(
					context, 
					android.R.layout.simple_spinner_item, 
					items);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
			typeSpinner.setAdapter(adapter);
			typeSpinner.setSelection(selectedPosition);
		}
	}
}
