/**
 * Ipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.ipay.api.common;

import com.ipay.api.common.internal.util.IpaySignature;

/**
 * @author liuqun.lq
 * @version $Id: DefaultSignChecker.java, v 0.1 2018��07��03�� 12:06 liuqun.lq Exp $
 */
public class DefaultSignChecker implements SignChecker {

    private String ipayPublicKey;

    public DefaultSignChecker(String ipayPublicKey) {
        this.ipayPublicKey = ipayPublicKey;
    }

    public boolean check(String sourceContent, String signature, String signType, String charset) {
        boolean success = false;
        try {
            success = IpaySignature.rsaCheck(sourceContent, signature, ipayPublicKey, charset, signType);
        } catch (IpayApiException e) {
            throw new RuntimeException(e);
        }
        return success;
    }

    /**
     * Getter method for property <tt>ipayPublicKey</tt>.
     *
     * @return property value of ipayPublicKey
     */
    public String getIpayPublicKey() {
        return ipayPublicKey;
    }

    /**
     * Setter method for property <tt>ipayPublicKey</tt>.
     *
     * @param ipayPublicKey value to be assigned to property ipayPublicKey
     */
    public void setIpayPublicKey(String ipayPublicKey) {
        this.ipayPublicKey = ipayPublicKey;
    }
}