package com.example.base;


import com.example.mobilefamilyportal.R;

import android.app.AlertDialog;
import android.content.Context;

public class BaseMethod {
	
	/*显示提示信息*/
	public static void ShowInformation(Context context, int title, int message){
		new AlertDialog.Builder(context)
			.setTitle(title)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage(message)
			.setPositiveButton(R.string.ok, null)
			.show();
	}
}
