package test;

import com.aliyuncs.exceptions.ClientException;
import com.garry.zboot.common.utils.SmsServiceUtil;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String args[]){
        Map<String,String> map = new HashMap();
        map.put("phoneNumber","13818536490");
        map.put("msgSign","");
        map.put("templateCode","SMS_170515175");
        map.put("jsonContent","zgj1234");
        try {
            SmsServiceUtil.sendSms(map);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
