package com.example.base;

public class BaseField {
	/* ��WCF ServiceContract�е�Namespaceһ�� */
	public static final String NAMESPACE="http://chad.cao";
	/* WCF��iis�еĵ���·��(http://������/����Ŀ¼/����) */
    public static final String URL="http://192.168.2.102/FamilyPortal-sl/MobileFamilyPortalService.svc";
    /* Namespace/����ӿ�/����  (����ķ���ʱ�Ƕ�̬��)
     * ����http://chad.cao/IService1/������ӷ���HelloWorld
     * ��http://chad.cao/IService1/HelloWorld*/
    public static final String PART_SOAP_ACTION="http://chad.cao/IMobileFamilyPortalService/";
	/*WCF������*/
	public static final String SYNCDAILYCONSUME="SyncDailyConsume";
	/*����WCF��ʱʱ��*/
	public static final int TIME_OUT=30000;
	
	public static final String DATABASE_NAME = "MobileFamilyPortal";//���ݿ������
	public static final String TABLE_NAME_USERINFO="UserInfo";//�û���
	public static final String TABLE_NAME_PARAINFO="ParaInfo";//������Ϣ��
	public static final String TABLE_NAME_PARADETAIL="ParaDetail";//������ϸ��
	public static final String TABLE_NAME_BANK_CARD="BankCard";//���п���
	public static final String TABLE_NAME_DAILY_CONSUME="DailyConsume";//�ճ����ѱ�
	public static final String TABLE_NAME_CONSUME="Consume";//����
	public static final int DATABASEVERSION = 1;//�汾��
	/*�˵�*/
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
	/*��¼��¼�û���userID*/
	public static int LOGIN_USER_ID=0;
	/*��¼��¼�û���userName*/
	public static String LOGIN_USER_NAME="";
	/*����Ա��userID=1*/
	public static final int ADMIN_USER_ID=1;
	/*������Ա��*/
	public static final String UI_TAG="UI_TEST";
	/*���ݿ���Ա��*/
	public static final String DATABASE_TAG="DATABASE_TEST";
	/*�ڲ�����Ϣ����
	 * ���п����͵ı����1
	 * ���п����еı����2
	 * �������͵ı����    3*/
	public static final int CARD_TYPE=1;
	public static final int CARD_CITY=2;
	public static final int CONSUME_TYPE=3;
	
	public static final String WARM_PROMPT="Warm prompt";
	public static final String SYNC_DAILYCONSUME="Sync daily consume to clouds.";
}
