/**
 * Ipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.ipay.api.common;

import com.ipay.api.common.internal.util.IpaySignature;

/**
 * Ĭ�ϼ�ǩ��
 *
 * @author liuqun.lq
 * @version $Id: DefaultSigner.java, v 0.1 2018��07��03�� 12:02 liuqun.lq Exp $
 */
public class DefaultSigner implements Signer {

    private String privateKey;

    public DefaultSigner(String privateKey) {
        this.privateKey = privateKey;
    }

    public String sign(String sourceContent, String signType, String charset) {
        String sign = null;
        try {
            sign = IpaySignature.rsaSign(sourceContent, this.privateKey, charset, signType);
        } catch (IpayApiException e) {
            throw new RuntimeException(e);
        }
        return sign;
    }

    /**
     * Getter method for property <tt>privateKey</tt>.
     *
     * @return property value of privateKey
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * Setter method for property <tt>privateKey</tt>.
     *
     * @param privateKey value to be assigned to property privateKey
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}