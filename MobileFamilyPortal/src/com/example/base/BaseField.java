package com.example.base;

public class BaseField {
	public static final String DATABASE_NAME = "MobileFamilyPortal";//数据库的名字
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
	/*ResultCode*/
	public static final int ADD_SUCCESSFULLY=1002;
	public static final int ADD_UNSUCCESSFULLY=1003;
	public static final int ADD_CANCEL=1004;
	public static final int UPDATE_SUCCESSFULLY=2002;
	public static final int UPDATE_UNSUCCESSFULLY=2003;
	public static final int UPDATE_CANCEL=2004;
}
