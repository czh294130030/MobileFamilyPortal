package com.example.base;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.mobilefamilyportal.R;
import com.example.model.ParaDetail;
import com.example.model.ParaInfo;
import com.example.model.UserInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Menu;


public class BaseMethod {
	
	/*����ǰCalendarת����int(int ���͵ķ�Χ�� -2147483648~2147483647)*/
	public static int convertCalendarToInt(Calendar calendar){
		int result=0;
		//int mYear=calendar.get(Calendar.YEAR);  
		//int mMonth=calendar.get(Calendar.MONTH);  
		//int mDay=calendar.get(Calendar.DAY_OF_MONTH);  
		int mHour=calendar.get(Calendar.HOUR_OF_DAY);  
		int mMinute=calendar.get(Calendar.MINUTE); 
		int mSecond=calendar.get(Calendar.SECOND);
		int mMillisecond=calendar.get(Calendar.MILLISECOND);
		/*int ���͵ķ�Χ�� -2147483648~2147483647*/
		String str=String.valueOf(mHour)+String.valueOf(mMinute)+String.valueOf(mSecond)+String.valueOf(mMillisecond);		
		result=Integer.parseInt(str);
		return result;
	}
	/*�����꣬�£��ջ�ȡ��ǰ����(October 11, 2013)*/
	public static String getCurrentDate(int year, int month, int day){
		String dateString="";
		dateString=year+"-"+(month+1)+"-"+day;
		return dateString;
	}
	/*��ȡ��ǰ��(October)*/
	/*private static String convertMonth(int month){
		String monthString="";
		switch (month) {
		case 0:
			monthString="Jan";
			break;
		case 1:
			monthString="Feb";
			break;
		case 2:
			monthString="Mar";
			break;
		case 3:
			monthString="Apr";
			break;
		case 4:
			monthString="May";
			break;
		case 5:
			monthString="Jun";
			break;
		case 6:
			monthString="Jul";
			break;
		case 7:
			monthString="Aug";
			break;
		case 8:
			monthString="Sep";
			break;
		case 9:
			monthString="Oct";
			break;
		case 10:
			monthString="Nov";
			break;
		case 11:
			monthString="Dec";
			break;
		default:
			break;
		}
		return monthString;
	}*/
	/*��ʾ��ʾ��Ϣ*/
	public static void showInformation(Context context, int title, int message){
		new AlertDialog.Builder(context)
			.setTitle(title)
			.setIcon(R.drawable.alert_info)
			.setMessage(message)
			.setPositiveButton(R.string.ok, null)
			.show();
	}
	
	/* ���÷�����Ƶ���MenuBuilder��setOptionalIconsVisible��������mOptionalIconsVisibleΪtrue��  
	 * ���˵�����ͼ��ʱ�ſɼ�  
	 * enableΪtrueʱ���˵����ͼ����Ч��enableΪfalseʱ��Ч��4.0ϵͳĬ����Ч   
	 */    
    public static void setIconEnable(Menu menu, boolean enable)    
    {    
        try{    
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");    
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);    
            m.setAccessible(true);   
            //MenuBuilderʵ��Menu�ӿڣ������˵�ʱ����������menu��ʵ����MenuBuilder����(java�Ķ�̬����)    
            m.invoke(menu, enable);    
        } catch (Exception e){    
            e.printStackTrace();    
        }    
    } 
    /*��ȡ����Ա��������*/
    public static UserInfo getAdminInfo(){
    	UserInfo itemInfo=new UserInfo();
    	itemInfo.setAccount("admin");
    	itemInfo.setUserName("admin.cao");
    	itemInfo.setPassword("123456");
    	return itemInfo;
    }
    /*��ȡ���õ�ParaInfo*/
    public static List<ParaInfo> getParaInfos(){
    	List<ParaInfo> items=new ArrayList<ParaInfo>();
    	/*��������*/
    	ParaInfo item1=new ParaInfo();
    	item1.setDescription("bank");
    	items.add(item1);
    	/*����*/
    	ParaInfo item2=new ParaInfo();
    	item2.setDescription("city");
    	items.add(item2);
    	/*��������*/
    	ParaInfo item3=new ParaInfo();
    	item3.setDescription("consume type");
    	items.add(item3);
    	return items;
    }
    /*��ȡ���õ�ParaDetail*/
    public static List<ParaDetail> getParaDetails(){
    	List<ParaDetail> items=new ArrayList<ParaDetail>();
    	/*��������*/
    	ParaDetail item1=new ParaDetail();
    	item1.setDescription("Bank of China");
    	item1.setInfoID(BaseField.CARD_TYPE);
    	items.add(item1);
    	ParaDetail item2=new ParaDetail();
    	item2.setDescription("ICBC");
    	item2.setInfoID(BaseField.CARD_TYPE);
    	items.add(item2);
    	ParaDetail item3=new ParaDetail();
    	item3.setDescription("ABC");
    	item3.setInfoID(BaseField.CARD_TYPE);
    	items.add(item3);
    	ParaDetail item4=new ParaDetail();
    	item4.setDescription("Bank of Communications");
    	item4.setInfoID(BaseField.CARD_TYPE);
    	items.add(item4);
    	ParaDetail item5=new ParaDetail();
    	item5.setDescription("China Construction Bank");
    	item5.setInfoID(BaseField.CARD_TYPE);
    	items.add(item5);
    	ParaDetail item6=new ParaDetail();
    	item6.setDescription("China Citic Bank");
    	item6.setInfoID(BaseField.CARD_TYPE);
    	items.add(item6);
    	ParaDetail item7=new ParaDetail();
    	item7.setDescription("China Merchants Bank");
    	item7.setInfoID(BaseField.CARD_TYPE);
    	items.add(item7);
    	ParaDetail item8=new ParaDetail();
    	item8.setDescription("Bank of JiangSu");
    	item8.setInfoID(BaseField.CARD_TYPE);
    	items.add(item8);
    	ParaDetail item9=new ParaDetail();
    	item9.setDescription("Others");
    	item9.setInfoID(BaseField.CARD_TYPE);
    	items.add(item9);
    	/*����*/
    	ParaDetail item10=new ParaDetail();
    	item10.setDescription("suzhou");
    	item10.setInfoID(BaseField.CARD_CITY);
    	items.add(item10);
    	ParaDetail item11=new ParaDetail();
    	item11.setDescription("jiaxing");
    	item11.setInfoID(BaseField.CARD_CITY);
    	items.add(item11);
    	ParaDetail item12=new ParaDetail();
    	item12.setDescription("yaan");
    	item12.setInfoID(BaseField.CARD_CITY);
    	items.add(item12);
    	/*��������*/
    	ParaDetail item13=new ParaDetail();
    	item13.setDescription("food");
    	item13.setInfoID(BaseField.CONSUME_TYPE);
    	items.add(item13);
    	ParaDetail item14=new ParaDetail();
    	item14.setDescription("clothing");
    	item14.setInfoID(BaseField.CONSUME_TYPE);
    	items.add(item14);
    	ParaDetail item15=new ParaDetail();
    	item15.setDescription("shelter");
    	item15.setInfoID(BaseField.CONSUME_TYPE);
    	items.add(item15);
    	ParaDetail item16=new ParaDetail();
    	item16.setDescription("transportation");
    	item16.setInfoID(BaseField.CONSUME_TYPE);
    	items.add(item16);
    	ParaDetail item17=new ParaDetail();
    	item17.setDescription("others");
    	item17.setInfoID(BaseField.CONSUME_TYPE);
    	items.add(item17);
    	return items;
    }
    /*�������п��ı�Ż�ȡ���п�ͼ��ı��*/
    public static int getBankIcoIDByBankID(int _bankID){
    	int bankIcoID=0;
    	switch (_bankID) {
		case 1:
			bankIcoID=R.drawable.bank_of_china;
			break;
		case 2:
			bankIcoID=R.drawable.icbc;
			break;
		case 3:
			bankIcoID=R.drawable.abc;
			break;
		case 4:
			bankIcoID=R.drawable.bank_of_communications;
			break;
		case 5:
			bankIcoID=R.drawable.china_construction_bank;
			break;
		case 6:
			bankIcoID=R.drawable.china_citic_bank;
			break;
		case 7:
			bankIcoID=R.drawable.china_merchants_bank;
			break;
		case 8:
			bankIcoID=R.drawable.bank_of_jiangsu;
			break;
		default:
			bankIcoID=R.drawable.other_banks;
			break;
		}
    	return bankIcoID;
    }
}
