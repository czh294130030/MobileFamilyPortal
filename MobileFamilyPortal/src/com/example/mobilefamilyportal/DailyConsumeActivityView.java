package com.example.mobilefamilyportal;

import java.util.List;

import com.example.dal.DailyConsumeDAL;
import com.example.model.Consume;
import com.example.model.DailyConsume;
import com.example.mycontrol.MyConsumeControlView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DailyConsumeActivityView extends Activity {

	private LinearLayout consumeLinearLayout=null;
	private TextView amountTextView=null;
	private TextView dateTextView=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_dailyconsumeview);
	    consumeLinearLayout=(LinearLayout)findViewById(R.id.consumeLinearLayout);
	    amountTextView=(TextView)findViewById(R.id.amountTextView);
	    dateTextView=(TextView)findViewById(R.id.dateTextView);
	    Bundle bundle=this.getIntent().getExtras();
	    int id=bundle.getInt("id");
	    DailyConsume model=getDailyConsume(id, true);
	    if(model!=null){
	    	amountTextView.setText(String.valueOf(model.getAmount()));
	    	dateTextView.setText(model.getDate());
	    	bindConsumeView(model.getConsumeList());
	    }
	}
	/*绑定所有的自定义消费控件*/
	private void bindConsumeView(List<Consume> list){
		if(list.size()>0){
			for (Consume consume : list) {
				addConsumeView(consume);
			}
		}
	}
	/*添加自定义查看消费空间*/
	private void addConsumeView(Consume consume){
		MyConsumeControlView myConsumeControlView=new MyConsumeControlView(DailyConsumeActivityView.this, null, consume);
		consumeLinearLayout.addView(myConsumeControlView);
	}
	/*根据日常消费编号获取日常消费Info&Details*/
	private DailyConsume getDailyConsume(int id, boolean isNeedDetails){
		DailyConsume item=new DailyConsume();
		item.setDailyID(id);
		DailyConsumeDAL dailyConsumeDAL=new DailyConsumeDAL(DailyConsumeActivityView.this);
		DailyConsume model=dailyConsumeDAL.queryModel(item, isNeedDetails);
		dailyConsumeDAL.close();
		return model;
	}
}
