/**
 * Ipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.ipay.api.common;

/**
 * �������ӿ�
 *
 * @author liuqun.lq
 * @version $Id: Encryptor.java, v 0.1 2018��07��03�� 11:41 liuqun.lq Exp $
 */
public interface Encryptor {

    /**
     * �����ݼ���
     *
     * @param sourceContent ����ǩ����
     * @param encryptType   �����㷨���ͣ���AES
     * @param charset       �ַ���
     * @return ���ܺ�����
     */
    String encrypt(String sourceContent, String encryptType, String charset);
}