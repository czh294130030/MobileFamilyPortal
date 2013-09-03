package com.example.base;


import java.lang.reflect.Method;
import com.example.mobilefamilyportal.R;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Menu;

public class BaseMethod {
	
	/*显示提示信息*/
	public static void showInformation(Context context, int title, int message){
		new AlertDialog.Builder(context)
			.setTitle(title)
			.setIcon(R.drawable.alert_info)
			.setMessage(message)
			.setPositiveButton(R.string.ok, null)
			.show();
	}
	
	/* 利用反射机制调用MenuBuilder的setOptionalIconsVisible方法设置mOptionalIconsVisible为true，  
	 * 给菜单设置图标时才可见  
	 * enable为true时，菜单添加图标有效，enable为false时无效。4.0系统默认无效   
	 */    
    public static void setIconEnable(Menu menu, boolean enable)    
    {    
        try{    
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");    
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);    
            m.setAccessible(true);   
            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)    
            m.invoke(menu, enable);    
        } catch (Exception e){    
            e.printStackTrace();    
        }    
    } 
}
