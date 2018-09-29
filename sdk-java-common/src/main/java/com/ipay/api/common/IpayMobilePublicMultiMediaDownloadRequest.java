/**
 * Ipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.ipay.api.common;

import com.ipay.api.common.internal.util.IpayHashMap;
import com.ipay.api.common.request.IpayRequest;

import java.io.OutputStream;
import java.util.Map;

/**
 * 多媒体文件下载请求
 *
 * @author yikai.hu
 * @version $Id: IpayMobilePublicMultiMediaDownloadRequest.java, v 0.1 Aug 15, 2014 10:19:15 AM yikai.hu Exp $
 */
public class IpayMobilePublicMultiMediaDownloadRequest implements
        IpayRequest<IpayMobilePublicMultiMediaDownloadResponse> {

    private IpayHashMap udfParams;         // add user-defined text parameters
    private String apiVersion = "1.0";

    private String notifyUrl;

    private OutputStream outputStream;

    private String bizContent;

    public void setBizContent(String bizContent) {
        this.bizContent = bizContent;
    }

    public String getBizContent() {
        return this.bizContent;
    }

    private String terminalType;
    private String terminalInfo;
    private String prodCode;

    public String getApiVersion() {
        return this.apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalType() {
        return this.terminalType;
    }

    public void setTerminalInfo(String terminalInfo) {
        this.terminalInfo = terminalInfo;
    }

    public String getTerminalInfo() {
        return this.terminalInfo;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdCode() {
        return this.prodCode;
    }

    public String getApiMethodName() {
        return "ipay.mobile.public.multimedia.download";
    }

    public Map<String, String> getTextParams() {
        IpayHashMap txtParams = new IpayHashMap();
        txtParams.put("biz_content", this.bizContent);
        if (udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        return txtParams;
    }

    public void putOtherTextParam(String key, String value) {
        if (this.udfParams == null) {
            this.udfParams = new IpayHashMap();
        }
        this.udfParams.put(key, value);
    }

    /**
     * Getter method for property <tt>outputStream</tt>.
     *
     * @return property value of outputStream
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Setter method for property <tt>outputStream</tt>.
     *
     * @param outputStream value to be assigned to property outputStream
     */
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Getter method for property <tt>notifyUrl</tt>.
     *
     * @return property value of notifyUrl
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * Setter method for property <tt>notifyUrl</tt>.
     *
     * @param notifyUrl value to be assigned to property notifyUrl
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Class<IpayMobilePublicMultiMediaDownloadResponse> getResponseClass() {
        return IpayMobilePublicMultiMediaDownloadResponse.class;
    }

    /**
     * @see IpayRequest#isNeedEncrypt()
     */
    public boolean isNeedEncrypt() {
        return false;
    }

    /**
     * @see IpayRequest#setNeedEncrypt(boolean)
     */
    public void setNeedEncrypt(boolean needEncrypt) {

        throw new RuntimeException("当前请求不支持加密！");
    }

    public String getReturnUrl() {
        return null;
    }

    public void setReturnUrl(String returnUrl) {
    }

    public IpayObject getBizModel() {
        return null;
    }

    public void setBizModel(IpayObject bizModel) {
    }

}
