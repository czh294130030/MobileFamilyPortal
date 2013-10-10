package com.example.mobilefamilyportal;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class DailyConsumeActivityOP extends Activity {

	
	private ImageButton calendarImageButton=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_dailyconsumeop);
	    calendarImageButton=(ImageButton)findViewById(R.id.calendarImageButton);
	    calendarImageButton.setOnTouchListener(new ImageButton.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					v.setBackgroundColor(getResources().getColor(R.color.button_mouseover_color));
				}else if(event.getAction()==MotionEvent.ACTION_UP){
					v.setBackgroundColor(getResources().getColor(R.color.transparent));
				}
				return false;
			}
		});
	}

}
