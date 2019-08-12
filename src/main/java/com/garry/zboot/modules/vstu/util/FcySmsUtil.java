package com.garry.zboot.modules.vstu.util;

import com.garry.zboot.common.utils.DateUtil;
import com.google.gson.JsonObject;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FcySmsUtil {

    private static String MD5(String sourceStr) {
        StringBuffer buffer = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buffer.append("0");
                }
                buffer.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static String encrypt(String content, String key) {

        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = content.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(),"AES");
            IvParameterSpec ivspec = new IvParameterSpec(key.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return parseByte2HexStr(encrypted);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;

    }


    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static String getTradeNo() {
        return DateUtil.getNowDateByFormate("yyyyMMddHHmmssSSS") + (int) (Math.random() * (1000 - 100) + 100);
    }
    public static String sendSms(String content,String phone) throws IOException {
        String tradeNo = getTradeNo();
        String password = MD5("bVeBktqk");
        String account = "vrarjiaoyu";
        String url = "http://apis.hzfacaiyu.com/sms/openCard";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName",account);
        jsonObject.put("phones",phone);
        jsonObject.put("content",content);
        jsonObject.put("userPassword",password);
        jsonObject.put("tradeNo",tradeNo);
        String sign = encrypt( jsonObject.toString(),"cNd6xbogqcDjqxHH");
        jsonObject.put("sign",sign);
        return HttpPostWithJson(url, jsonObject.toString());

    }

    public static void main(String args[]) {
        int code = (int) ((Math.random() * 999999) + 100);
        System.out.println(code);
       /* String tradeNo = getTradeNo();
        String content = "【VR教育】注册短信验证码：09890";
        String password = MD5("bVeBktqk");
        String account = "vrarjiaoyu";
        String url = "http://apis.hzfacaiyu.com/sms/openCard";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName",account);
        jsonObject.put("phones","13818536490");
        jsonObject.put("content",content);
        jsonObject.put("userPassword",password);
        jsonObject.put("tradeNo",tradeNo);
        String sign = encrypt( jsonObject.toString(),"cNd6xbogqcDjqxHH");
        jsonObject.put("sign",sign);
        System.out.println("======================");
        System.out.println("======================");
        System.out.println("======================");
        System.out.println(jsonObject.toString());
        System.out.println("======================");
        System.out.println("======================");
        System.out.println("======================");
        try {
//            HttpClientPost();
            System.out.println(HttpPostWithJson(url, jsonObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    public static void HttpClientPost() throws Exception {
        String tradeNo = getTradeNo();
        String content = "这是一条短信";
        String password = MD5("bVeBktqk");
        String sign = encrypt( content,"cNd6xbogqcDjqxHH");
        String account = "vrarjiaoyu";
        // 获取默认的请求客户端
        CloseableHttpClient client = HttpClients.createDefault();
        // 通过HttpPost来发送post请求
        String url = "http://apis.hzfacaiyu.com/sms/openCard";
        HttpPost httpPost = new HttpPost(url);
        /*
         * post带参数开始
         */
        // 第三步：构造list集合，往里面丢数据
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        BasicNameValuePair userNameP = new BasicNameValuePair("userName", account);
        BasicNameValuePair userPasswordP = new BasicNameValuePair("userPassword", password);
        BasicNameValuePair tradeNoP = new BasicNameValuePair("tradeNo", tradeNo);
        BasicNameValuePair contentP = new BasicNameValuePair("content", content);
        BasicNameValuePair phonesP = new BasicNameValuePair("phones", "13818536490");
        BasicNameValuePair signP = new BasicNameValuePair("sign", sign);
        list.add(userNameP);
        list.add(userPasswordP);
        list.add(tradeNoP);
        list.add(contentP);
        list.add(phonesP);
        list.add(signP);
        // 第二步：我们发现Entity是一个接口，所以只能找实现类，发现实现类又需要一个集合，集合的泛型是NameValuePair类型
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list);
        // 第一步：通过setEntity 将我们的entity对象传递过去
        httpPost.setEntity(formEntity);
        /*
         * post带参数结束
         */
        // HttpEntity
        // 是一个中间的桥梁，在httpClient里面，是连接我们的请求与响应的一个中间桥梁，所有的请求参数都是通过HttpEntity携带过去的
        // 通过client来执行请求，获取一个响应结果
        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String str = EntityUtils.toString(entity, "UTF-8");
        System.out.println(str);
        // 关闭
        response.close();
    }

    public static String HttpPostWithJson(String url, String json) throws IOException {
        String returnValue = "这是默认返回值，接口调用失败";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try{
            httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(url);

            StringEntity requestEntity = new StringEntity(json,"utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);

            //第四步：发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法

        }
        finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //第五步：处理返回值
        return returnValue;
    }
}
