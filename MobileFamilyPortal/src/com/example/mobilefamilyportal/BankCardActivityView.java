package com.example.mobilefamilyportal;

import com.example.base.BaseMethod;
import com.example.dal.BankCardDAL;
import com.example.dal.ParaDetailDAL;
import com.example.dal.UserInfoDAL;
import com.example.model.BankCard;
import com.example.model.ParaDetail;
import com.example.model.UserInfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BankCardActivityView extends Activity {

	private TextView cardTypeTextView=null;
	private TextView cardNOTextView=null;
	private TextView cardUserTextView=null;
	private TextView cardCityTextView=null;
	private ImageView icoImageView=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_bankcardview);
	    cardTypeTextView=(TextView)findViewById(R.id.cardTypeTextView);
	    cardNOTextView=(TextView)findViewById(R.id.cardNoTextView);
	    cardUserTextView=(TextView)findViewById(R.id.cardUserTextView);
	    cardCityTextView=(TextView)findViewById(R.id.cardCityTextView);
	    icoImageView=(ImageView)findViewById(R.id.icoImageView);
	    Bundle bundle=this.getIntent().getExtras();
	    int id=bundle.getInt("id");
	    bind(id);
	}
	/*�������п���Ż�ȡ���п���Ϣ�󶨱�*/
	private void bind(int _id){
		BankCard bankCard=getBankCard(_id);
		if(bankCard!=null){
			/*��ȡ������*/
			ParaDetail cardType=getParaDetail(bankCard.getBankID());
			icoImageView.setImageResource(BaseMethod.getBankIcoIDByBankID(bankCard.getBankID()));
			if(cardType!=null){
				cardTypeTextView.setText(cardType.getDescription());
			}
			/*��ȡ�����*/
			cardNOTextView.setText(bankCard.getCardNO());
			/*��ȡ���û�*/
			UserInfo userInfo=getUserInfo(bankCard.getUserID());
			if(userInfo!=null){
				cardUserTextView.setText(userInfo.getUserName());
			}
			/*��ȡ������*/
			ParaDetail cardCity=getParaDetail(bankCard.getCityID());
			if(cardCity!=null){
				cardCityTextView.setText(cardCity.getDescription());
			}
		}
	}
	/*���ݲ�����ϸ��Ż�ȡ������Ϣ*/
	private ParaDetail getParaDetail(int _detailID){
		ParaDetail item=new ParaDetail();
		item.setDetailID(_detailID);
		ParaDetailDAL paraDetailDAL=new ParaDetailDAL(this);
		ParaDetail model=paraDetailDAL.queryModel(item);
		paraDetailDAL.close();
		return model;
	}
	/*�����û���Ż�ȡ�û���Ϣ*/
	private UserInfo getUserInfo(int _userID){
		UserInfo item=new UserInfo();
		item.setUserID(_userID);
		UserInfoDAL userInfoDAL=new UserInfoDAL(this);
		UserInfo model=userInfoDAL.queryModel(item);
		userInfoDAL.close();
		return model;
	}
	/*�������п���Ż�ȡ���п�*/
	private BankCard getBankCard(int _id){
		BankCard item=new BankCard();
		item.setCardID(_id);
		BankCardDAL bankCardDAL=new BankCardDAL(this);
		BankCard model=bankCardDAL.queryModel(item);
		bankCardDAL.close();
		return model;
	}
}
