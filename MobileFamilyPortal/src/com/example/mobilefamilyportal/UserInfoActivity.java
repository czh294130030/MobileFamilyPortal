package com.example.mobilefamilyportal;

import com.example.dal.UserInfoDAL;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class UserInfoActivity extends Activity {

	private ListView userInfoListView=null;
	private UserInfoDAL userInfoDAL=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_userinfo);
	    userInfoListView=(ListView)findViewById(R.id.userInfoListView);
	    userInfoDAL=new UserInfoDAL(this);
	}
}
