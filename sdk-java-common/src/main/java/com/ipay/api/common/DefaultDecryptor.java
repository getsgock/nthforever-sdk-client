/**
 * Ipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.ipay.api.common;

import com.ipay.api.common.internal.util.IpayEncrypt;

/**
 * @author liuqun.lq
 * @version $Id: DefaultDecryptor.java, v 0.1 2018��07��03�� 12:35 liuqun.lq Exp $
 */
public class DefaultDecryptor implements Decryptor {

    private String encryptKey;

    public DefaultDecryptor(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public String decrypt(String encryptContent, String encryptType, String charset) {
        String decryptContent = null;
        try {
            decryptContent = IpayEncrypt.decryptContent(encryptContent, encryptType,
                    encryptKey, charset);
        } catch (IpayApiException e) {
            throw new RuntimeException(e);
        }
        return decryptContent;
    }

    /**
     * Getter method for property <tt>encryptKey</tt>.
     *
     * @return property value of encryptKey
     */
    public String getEncryptKey() {
        return encryptKey;
    }

    /**
     * Setter method for property <tt>encryptKey</tt>.
     *
     * @param encryptKey value to be assigned to property encryptKey
     */
    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }
}