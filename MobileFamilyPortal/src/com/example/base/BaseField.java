package com.example.base;

public class BaseField {
	/* 和WCF ServiceContract中的Namespace一致 */
	public static final String NAMESPACE="http://chad.cao";
	/* WCF在iis中的调用路径(http://服务器/虚拟目录/服务) */
    public static final String URL="http://192.168.2.102/FamilyPortal-sl/MobileFamilyPortalService.svc";
    /* Namespace/服务接口/方法  (这里的方法时是动态的)
     * 如在http://chad.cao/IService1/后面添加方法HelloWorld
     * 成http://chad.cao/IService1/HelloWorld*/
    public static final String PART_SOAP_ACTION="http://chad.cao/IMobileFamilyPortalService/";
	/*WCF方法名*/
	public static final String SYNCDAILYCONSUME="SyncDailyConsume";
	/*访问WCF超时时间*/
	public static final int TIME_OUT=30000;
	
	public static final String DATABASE_NAME = "MobileFamilyPortal";//数据库的名字
	public static final String TABLE_NAME_USERINFO="UserInfo";//用户表
	public static final String TABLE_NAME_PARAINFO="ParaInfo";//参数信息表
	public static final String TABLE_NAME_PARADETAIL="ParaDetail";//参数明细表
	public static final String TABLE_NAME_BANK_CARD="BankCard";//银行卡表
	public static final String TABLE_NAME_DAILY_CONSUME="DailyConsume";//日常消费表
	public static final String TABLE_NAME_CONSUME="Consume";//消费
	public static final int DATABASEVERSION = 1;//版本号
	/*菜单*/
	public static final int ADD=0;
	public static final int EDIT=1;
	public static final int DELETE=2;
	public static final int OK=3;
	public static final int CANCEL=4;
	public static final int VIEW=5;
	public static final int SYNC=6;
	/*requestCode*/
	public static final int ADD_USERINFO=1001;
	public static final int EDIT_USERINFO=2001;
	public static final int LOGIN=3001;
	public static final int ADD_BANK_CARD=4001;
	public static final int EDIT_BANK_CARD=5001;
	public static final int ADD_DAILY_CONSUME=6001;
	public static final int EDIT_DAILY_CONSUME=7001;
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
	/*在参数信息表中
	 * 银行卡类型的编号是1
	 * 银行卡城市的编号是2
	 * 消费类型的编号是    3*/
	public static final int CARD_TYPE=1;
	public static final int CARD_CITY=2;
	public static final int CONSUME_TYPE=3;
	
	public static final String WARM_PROMPT="Warm prompt";
	public static final String SYNC_DAILYCONSUME="Sync daily consume to clouds.";
}
