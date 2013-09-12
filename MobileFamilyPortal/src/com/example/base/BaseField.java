package com.example.base;

public class BaseField {
	public static final String DATABASE_NAME = "MobileFamilyPortal";//数据库的名字
	public static final String TABLE_NAME_USERINFO="UserInfo";
	public static final String TABLE_NAME_PARAINFO="ParaInfo";
	public static final String TABLE_NAME_PARADETAIL="ParaDetail";
	public static final int DATABASEVERSION = 1;//版本号
	/*菜单*/
	public static final int ADD=0;
	public static final int EDIT=1;
	public static final int DELETE=2;
	public static final int OK=3;
	public static final int CANCEL=4;
	/*requestCode*/
	public static final int ADD_USERINFO=1001;
	public static final int EDIT_USERINFO=2001;
	public static final int LOGIN=3001;
	/*ResultCode*/
	public static final int ADD_SUCCESSFULLY=1002;
	public static final int ADD_UNSUCCESSFULLY=1003;
	public static final int ADD_CANCEL=1004;
	public static final int UPDATE_SUCCESSFULLY=2002;
	public static final int UPDATE_UNSUCCESSFULLY=2003;
	public static final int UPDATE_CANCEL=2004;
	public static final int LOGIN_SUCCESSFULLY=3002;
	public static final int LOGIN_CANCEL=3003;
	/*记录登录用户的userID*/
	public static int LOGIN_USER_ID=0;
	/*记录登录用户的userName*/
	public static String LOGIN_USER_NAME="";
	/*管理员的userID=1*/
	public static final int ADMIN_USER_ID=1;
	/*界面测试标记*/
	public static final String UI_TAG="UI_TEST";
	/*数据库测试标记*/
	public static final String DATABASE_TAG="DATABASE_TEST";
}
