package com.example.base;

import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.model.DailyConsume;

public class SoapService implements ISoapService{

    public boolean syncDailyConsume(List<DailyConsume> _dailyConsumes, String _methodName){
    	boolean flag=false;
    	String soapAction=BaseField.PART_SOAP_ACTION+_methodName;
    	String jsonString=DailyConsume.ConvertToJson(_dailyConsumes);
    	if(jsonString.equals("")){return true;}
    	SoapObject soapObject=new SoapObject(BaseField.NAMESPACE, _methodName); 
    	/*传参，记住参数名必须和WCF方法中的参数名一致   */ 
        soapObject.addProperty("_jsonString", jsonString);
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11); 
        envelope.bodyOut=soapObject;    
        envelope.dotNet=true;    
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE transportSE=new HttpTransportSE(BaseField.URL, BaseField.TIME_OUT);
        transportSE.debug=true;//使用调式功能    
        try {    
        	transportSE.call(soapAction, envelope); 
            if(envelope.getResponse()!=null){
            	SoapObject resultObject=(SoapObject)envelope.bodyIn;
            	flag=Boolean.parseBoolean(resultObject.getProperty(0).toString());
            }
        }catch (Exception e) {
			e.printStackTrace();
		}
    	return flag;
    }
}
