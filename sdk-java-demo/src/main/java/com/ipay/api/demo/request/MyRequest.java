package com.ipay.api.demo.request;

import com.alibaba.fastjson.JSON;
import com.ipay.api.common.IpayObject;
import com.ipay.api.common.request.IpayRequest;
import com.ipay.api.demo.response.MyResponse;

import java.util.HashMap;
import java.util.Map;

public class MyRequest implements IpayRequest<MyResponse> {


    /**************************基础参数*********************************/
    private String bizContent;
    private String terminalType = "IOS";
    private String terminalInfo = "nthforever";
    private String prodCode = "ipayLinks";
    private String notifyUrl ="https://www.ipaylinks.com/cn/landing.html";
    private String returnUrl ="https://www.ipaylinks.com/cn/landing.html";
    private boolean needEncrypt=true;
    private IpayObject bizModel=null;
    /*****************************业务参数******************************/

    private String name;
    private String age;
    private String fruit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getFruit() {
        return fruit;
    }

    public void setFruit(String fruit) {
        this.fruit = fruit;
    }

    /***********************************************************/
    public String getBizContent() {
        return bizContent;
    }

    public void setBizContent(String bizContent) {
        this.bizContent = bizContent;
    }


    @Override
    public String getApiMethodName() {
        return "hello";
    }

    @Override
    public Map<String, String> getTextParams() {
        //通过BO形式设置参数
        Map txtParams = new HashMap();
        txtParams.put("name", this.name);
        txtParams.put("age", this.age);
        if (isNeedEncrypt()){
            bizContent = JSON.toJSONString(txtParams);
            txtParams.clear();
            txtParams.put("biz_content", bizContent );
        }else {

        }
        return txtParams;
    }

    public String getApiVersion() {
        return "3.0.1";
    }

    public void setApiVersion(String s) {

    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String s) {
        terminalType =s;
    }

    public String getTerminalInfo() {
        return terminalInfo;
    }

    public void setTerminalInfo(String s) {
        terminalInfo =s;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String s) {
        prodCode =s;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String s) {
        notifyUrl =s;
    }
    @Override
    public String getReturnUrl() {
        return returnUrl;
    }
    @Override
    public void setReturnUrl(String s) {
        returnUrl =s;
    }
    @Override
    public Class<MyResponse> getResponseClass() {
        return MyResponse.class;
    }
    @Override
    public boolean isNeedEncrypt() {
        return needEncrypt;
    }
    @Override
    public void setNeedEncrypt(boolean b) {
        this.needEncrypt = b;
    }
    @Override
    public IpayObject getBizModel() {
        return null;
    }
    @Override
    public void setBizModel(IpayObject ipayObject) {

    }
}
