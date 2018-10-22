package com.ipay.api.demo.webapp;

import com.ipay.api.common.*;
import com.ipay.api.common.DefaultIpayClient.Builder;
import com.ipay.api.common.internal.util.IpaySignature;
import com.ipay.api.demo.request.MyRequest;
import com.ipay.api.demo.response.MyResponse;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.SecureRandom;

public class Client {

    static final String SERVER_URL="http://localhost:8080/jiaqi/";
    static final String APP_ID="jiaqi";
    static final String PRIVATE_KEY="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIOd6Hk5jw8alL4zX+g5ZA+ujEpbdpMeKdBfbOegZI8NkAknLNh41Af0MiUXhQ0UUNZHfOB+ErKcFiAROgkwtSdjULxFmB/dPhQ8lWrOkh6axJcJ4+0O3vD8a9s89+saOIRMWxJ6bwV7WuXIj1h6FihM19AUnYCFzoxciCigaN8xAgMBAAECgYBldFV9DIdgJyc9LjDhu0uMYwo1Tyep4/hrbQNv0M1zEAtVYuNIwmr+7pewy9595Ikjg9uY2NXk5DiR43WPHAKnBuc/0SnJBxZrrmWE+X/PVbMO4g72XKhJi2yvQXUrPoqXl/QCgMaXHDstW9TgLbAc/oBBXpqF4puIsJ5buiXfgQJBAOHg7I4xCCyL9+J0nZQ5whzggQ058NFDP1lF940gK/bfVTsHV5fE21xU4uIvcBOv5qVy/br9O/0k6gMF+mF0cGkCQQCVKw/hB3fD/t6OCJULYCagcG6z/zAqCMFwcQGvjoxQAQdoGt7PXLQLVGhbNbILCoLPBoa6fzcJ1g84E/X0HB+JAkEAq5IhnM/5mXynyUcy+of0veJ5pAZCuXEPCUxwAK5TNq3lG3U0P3+z76o6u/u0cf/Gfh2eRQ5dJqeHaMx9ptC/IQJAENIAQmGzMZilw+JX3CaFdIX5Fbbq2XKPTXyQshki3UqgaS7OfRd4/eYQtmhBoVkYEvwraRA7RpKquKoXi+U38QJAJCvphVu0CRI8XfhYEQHTuHImunKv1RZcC+f589YMbkEvRoRXsD3inSpZpRhwjLstnQVZqM7nCgoDA8O2RjMOpQ==";
    static final String PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCDneh5OY8PGpS+M1/oOWQProxKW3aTHinQX2znoGSPDZAJJyzYeNQH9DIlF4UNFFDWR3zgfhKynBYgEToJMLUnY1C8RZgf3T4UPJVqzpIemsSXCePtDt7w/GvbPPfrGjiETFsSem8Fe1rlyI9YehYoTNfQFJ2Ahc6MXIgooGjfMQIDAQAB";
    static final String ENCRYPT_TYPE = "AES";
    static final String ENCRYPT_KEY = "00a51f3f48415c7d4e8908980d443c29";
    static final String SIGN_TYPE = "RSA";
    static final String CHAR_SET="UTF-8";

    public static void main(String[] args) {

        test1();


        if (true)
            return;
        String testStr="12345";
        try {
            getKey(ENCRYPT_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String encryptor = encryptor(testStr);
        String decryptor = decryptor(encryptor);

        String sign = sign(testStr);
        check(sign,testStr);






    }
    public static void test1(){
        Builder builder = DefaultIpayClient.builder(SERVER_URL, APP_ID, PRIVATE_KEY);
        builder.charset(IpayConstants.CHARSET_UTF8)
                .encryptType(ENCRYPT_TYPE)
                .ipayPublicKey(PUBLIC_KEY)
                .signType(SIGN_TYPE)
                .encryptKey(ENCRYPT_KEY);
        DefaultIpayClient client = builder.build();
        MyRequest request = new MyRequest();
        request.setName("zhang3");
        request.setAge("21");
        request.setFruit("apple");
        try {
            MyResponse execute = client.execute(request);
            System.out.println(execute);
        } catch (IpayApiException e) {
            e.printStackTrace();
        }
    }

    public static void test(){
        URL url= null;
        String message="";
        OutputStream out = null;
        try {
            url = new URL("http://localhost:8080/jiaqi/test/data");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type","application/x-www-form-urlencoded");//默认浏览器编码类型，
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(50*1000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            out = connection.getOutputStream();
            out.write("abc".getBytes());

            InputStream inputStream=connection.getInputStream();
            byte[] data=new byte[1024];
            StringBuffer sb=new StringBuffer();
            int length=0;
            while ((length=inputStream.read(data))!=-1){
                String s=new String(data, Charset.forName("utf-8"));
                sb.append(s);
            }
            message=sb.toString();
            System.out.println("message ==》 "+message);
            inputStream.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //加密
    public static String encryptor(String source){
        DefaultEncryptor defaultEncryptor = new DefaultEncryptor(ENCRYPT_KEY);
        String encrypt = defaultEncryptor.encrypt(source, ENCRYPT_TYPE, CHAR_SET);
        System.out.println("加密成功 ==》 "+encrypt);
        return encrypt;
    }

    //解密
    public static String decryptor(String source){
        DefaultDecryptor defaultDecryptor = new DefaultDecryptor(ENCRYPT_KEY);
        String decrypt = defaultDecryptor.decrypt(source, ENCRYPT_TYPE, CHAR_SET);
        System.out.println("解密成功 ==》 "+decrypt);
        return decrypt;
    }



    /**
     *
     * @param source
     * @return
     */
    public static String sign(String source){
        String res ="";
        try {
            res = IpaySignature.rsaSign(source, PRIVATE_KEY, CHAR_SET);
            System.out.println("加签成功 ==》 "+res);
        } catch (IpayApiException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @param sign
     * @param source
     * @return
     */
    public static boolean check(String sign,String source){
        boolean rsa = false;
        try {
            rsa = IpaySignature.rsaCheck(source, sign, PUBLIC_KEY, CHAR_SET, "RSA");
            System.out.println("验签是否成功 ==》 "+rsa);
        } catch (IpayApiException e) {
            e.printStackTrace();
        }
        return rsa;
    }


    /**
     * * 生成密钥
     * * @return
     * * @throws Exception
     * */
    public static Key getKey(String strKey) throws Exception {
        //创建密钥生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPT_TYPE);
        //初始化密钥
         keyGenerator.init(new SecureRandom(strKey.getBytes()));
         //生成密钥
        SecretKey getKey = keyGenerator.generateKey();
        System.out.println("生成密钥:"+byteToHexString(getKey.getEncoded ())+"----"+byteToHexString(getKey.getEncoded ()).length());
        return getKey;
    }
    /** *
     * byte数组转化为16进制字符串
     * *
     * @param bytes *
     * @return
     * */
    public static String byteToHexString(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex=Integer.toHexString(bytes[i]);
            if(strHex.length() > 3){
                sb.append(strHex.substring(6));
            } else {
                if(strHex.length() < 2){
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }     return  sb.toString();
    }

}
