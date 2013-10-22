package com.example.mycontrol;

import com.example.dal.ParaDetailDAL;
import com.example.mobilefamilyportal.R;
import com.example.model.Consume;
import com.example.model.ParaDetail;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyConsumeControlView extends LinearLayout{

	private TextView typeTextView=null;
	private TextView amountTextView=null;
	private TextView descriptionTextView=null;
	public MyConsumeControlView(Context context){
		super(context);
	}
	public MyConsumeControlView(Context context, AttributeSet attr, Consume consume){
		super(context, attr);
		LayoutInflater layoutInflater=
				(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.control_consumeview, this);
		typeTextView=(TextView)findViewById(R.id.typeTextView);
		amountTextView=(TextView)findViewById(R.id.amountTextView);
		descriptionTextView=(TextView)findViewById(R.id.descriptionTextView);
		bind(consume, context);
	}
	/*绑定日常消费信息*/
	private void bind(Consume consume, Context context){
		if(consume!=null){
			int typeID=consume.getTypeID();
			ParaDetail item=new ParaDetail();
			item.setDetailID(typeID);
			ParaDetailDAL paraDetailDAL=new ParaDetailDAL(context);
			ParaDetail model=paraDetailDAL.queryModel(item);
			paraDetailDAL.close();
			typeTextView.setText(model.getDescription());
			amountTextView.setText(String.valueOf(consume.getAmount()));
			descriptionTextView.setText(consume.getDescription());
		}
	}
}
