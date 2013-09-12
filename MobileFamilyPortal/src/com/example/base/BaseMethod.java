package com.example.base;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.example.mobilefamilyportal.R;
import com.example.model.ParaDetail;
import com.example.model.ParaInfo;
import com.example.model.UserInfo;

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
    /*获取管理员内置数据*/
    public static UserInfo getAdminInfo(){
    	UserInfo itemInfo=new UserInfo();
    	itemInfo.setAccount("admin");
    	itemInfo.setUserName("admin.cao");
    	itemInfo.setPassword("123456");
    	return itemInfo;
    }
    /*获取内置的ParaInfo*/
    public static List<ParaInfo> getParaInfos(){
    	List<ParaInfo> items=new ArrayList<ParaInfo>();
    	ParaInfo item1=new ParaInfo();
    	item1.setDescription("bank");
    	items.add(item1);
    	ParaInfo item2=new ParaInfo();
    	item2.setDescription("city");
    	items.add(item2);
    	return items;
    }
    /*获取内置的ParaDetail*/
    public static List<ParaDetail> getParaDetails(){
    	List<ParaDetail> items=new ArrayList<ParaDetail>();
    	ParaDetail item1=new ParaDetail();
    	item1.setDescription("Bank of China");
    	item1.setInfoID(1);
    	items.add(item1);
    	ParaDetail item2=new ParaDetail();
    	item2.setDescription("ICBC");
    	item2.setInfoID(1);
    	items.add(item2);
    	ParaDetail item3=new ParaDetail();
    	item3.setDescription("ABC");
    	item3.setInfoID(1);
    	items.add(item3);
    	ParaDetail item4=new ParaDetail();
    	item4.setDescription("Bank of Communications");
    	item4.setInfoID(1);
    	items.add(item4);
    	ParaDetail item5=new ParaDetail();
    	item5.setDescription("China Construction Bank");
    	item5.setInfoID(1);
    	items.add(item5);
    	ParaDetail item6=new ParaDetail();
    	item6.setDescription("China Citic Bank");
    	item6.setInfoID(1);
    	items.add(item6);
    	ParaDetail item7=new ParaDetail();
    	item7.setDescription("China Merchants Bank");
    	item7.setInfoID(1);
    	items.add(item7);
    	ParaDetail item8=new ParaDetail();
    	item8.setDescription("Bank of JiangSu");
    	item8.setInfoID(1);
    	items.add(item8);
    	ParaDetail item9=new ParaDetail();
    	item9.setDescription("Others");
    	item9.setInfoID(1);
    	items.add(item9);
    	ParaDetail item10=new ParaDetail();
    	item10.setDescription("suzhou");
    	item10.setInfoID(2);
    	items.add(item10);
    	ParaDetail item11=new ParaDetail();
    	item11.setDescription("jiaxing");
    	item11.setInfoID(2);
    	items.add(item11);
    	ParaDetail item12=new ParaDetail();
    	item12.setDescription("yaan");
    	item12.setInfoID(2);
    	items.add(item12);
    	return items;
    }
}
