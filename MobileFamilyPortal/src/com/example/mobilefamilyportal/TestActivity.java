package com.example.mobilefamilyportal;

import java.util.List;

import com.example.base.BaseField;
import com.example.dal.ParaDetailDAL;
import com.example.dal.ParaInfoDAL;
import com.example.model.KeyValue;
import com.example.mycontrol.MyConsumeControl;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class TestActivity extends Activity {

	private LinearLayout testLinearLayout=null;
	/*用来测试的Activity*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_test);
	    /*创建所有表和内置数据*/
		ParaInfoDAL paraInfoDAL=new ParaInfoDAL(this);
		paraInfoDAL.close();
		
		ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
		List<KeyValue> items=paraDetailDAL.queryKeyValueList("WHERE infoID="+BaseField.CONSUME_TYPE);
		
		/*后台添加自定义控件*/
		testLinearLayout=(LinearLayout)findViewById(R.id.testLinearLayout);
		MyConsumeControl myConsumeControl=new MyConsumeControl(TestActivity.this, null, items, false);
		testLinearLayout.addView(myConsumeControl);
	}
}
