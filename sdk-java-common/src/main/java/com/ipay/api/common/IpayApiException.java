/**
 * Ipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.ipay.api.common;


/**
 * @author runzhi
 */
public class IpayApiException extends Exception {

    private static final long serialVersionUID = -238091758285157331L;

    private String errCode;
    private String errMsg;

    public IpayApiException() {
        super();
    }

    public IpayApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IpayApiException(String message) {
        super(message);
    }

    public IpayApiException(Throwable cause) {
        super(cause);
    }

    public IpayApiException(String errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

}