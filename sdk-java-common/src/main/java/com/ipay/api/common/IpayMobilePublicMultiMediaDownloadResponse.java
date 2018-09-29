/**
 * Ipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.ipay.api.common;

import com.ipay.api.common.internal.mapping.ApiField;
import com.ipay.api.common.response.IpayResponse;

/**
 * 多媒体文件下载响应
 * @author yikai.hu
 * @version $Id: IpayMobilePublicMultiMediaDownloadResponse.java, v 0.1 Aug 15, 2014 10:19:31 AM yikai.hu Exp $
 */
public class IpayMobilePublicMultiMediaDownloadResponse extends IpayResponse {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4500718209713594926L;

    @ApiField("code")
    private String code;

    @ApiField("msg")
    private String msg;

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

}
