/**
 * Ipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.ipay.api.common;

/**
 * @author runzhi
 * @version $Id: DefaultIpayClient.java, v 0.1 2012-11-49:45:21 runzhi Exp $
 */
public class DefaultIpayClient extends AbstractIpayClient {

    private String privateKey;
    private String encryptKey;
    private String ipayPublicKey;
    private Signer signer;
    private SignChecker signChecker;
    private Encryptor encryptor;
    private Decryptor decryptor;

    public DefaultIpayClient(String serverUrl, String appId, String privateKey) {
        super(serverUrl, appId, null, null, null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
    }

    public DefaultIpayClient(String serverUrl, String appId, String privateKey, String format) {
        super(serverUrl, appId, format, null, null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
    }

    public DefaultIpayClient(String serverUrl, String appId, String privateKey, String format,
                             String charset) {
        super(serverUrl, appId, format, charset, null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
    }

    public DefaultIpayClient(String serverUrl, String appId, String privateKey, String format,
                             String charset, String ipayPublicKey) {
        super(serverUrl, appId, format, charset, null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.ipayPublicKey = ipayPublicKey;
        this.signChecker = new DefaultSignChecker(ipayPublicKey);
    }

    public DefaultIpayClient(String serverUrl, String appId, String privateKey, String format,
                             String charset, String ipayPublicKey, String signType) {
        super(serverUrl, appId, format, charset, signType);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.ipayPublicKey = ipayPublicKey;
        this.signChecker = new DefaultSignChecker(ipayPublicKey);
    }

    public DefaultIpayClient(String serverUrl, String appId, String privateKey, String format,
                             String charset, String ipayPublicKey, String signType,
                             String proxyHost, int proxyPort) {
        super(serverUrl, appId, format, charset, signType, proxyHost, proxyPort);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.ipayPublicKey = ipayPublicKey;
        this.signChecker = new DefaultSignChecker(ipayPublicKey);
    }

    public DefaultIpayClient(String serverUrl, String appId, String privateKey, String format,
                             String charset, String ipayPublicKey, String signType,
                             String encryptKey, String encryptType) {
        super(serverUrl, appId, format, charset, signType, encryptType);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.ipayPublicKey = ipayPublicKey;
        this.signChecker = new DefaultSignChecker(ipayPublicKey);
        this.encryptor = new DefaultEncryptor(encryptKey);
        this.decryptor = new DefaultDecryptor(encryptKey);
    }

    public Signer getSigner() {
        return signer;
    }

    public SignChecker getSignChecker() {
        return signChecker;
    }

    public Encryptor getEncryptor() {
        return encryptor;
    }

    public Decryptor getDecryptor() {
        return decryptor;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        if (this.signer == null) {
            this.signer = new DefaultSigner(privateKey);
        }
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
        if (this.encryptor == null) {
            this.encryptor = new DefaultEncryptor(encryptKey);
        }
        if (this.decryptor == null) {
            this.decryptor = new DefaultDecryptor(encryptKey);
        }
    }

    public void setIpayPublicKey(String ipayPublicKey) {
        this.ipayPublicKey = ipayPublicKey;
        if (this.signChecker == null) {
            this.signChecker = new DefaultSignChecker(ipayPublicKey);
        }
    }

    public static Builder builder(String serverUrl, String appId, String privateKey) {
        return new Builder(serverUrl, appId, privateKey);
    }

    public static class Builder {
        private DefaultIpayClient client;

        Builder(String serverUrl, String appId, String privateKey) {
            client = new DefaultIpayClient(serverUrl, appId, privateKey);
        }

        public DefaultIpayClient build() {
            return client;
        }

        public Builder prodCode(String prodCode) {
            client.setProdCode(prodCode);
            return this;
        }

        public Builder format(String format) {
            client.setFormat(format);
            return this;
        }

        public Builder signType(String signType) {
            client.setSignType(signType);
            return this;
        }

        public Builder encryptType(String encryptType) {
            client.setEncryptType(encryptType);
            return this;
        }

        public Builder encryptKey(String encryptKey) {
            client.setEncryptKey(encryptKey);
            return this;
        }

        public Builder ipayPublicKey(String ipayPublicKey) {
            client.setIpayPublicKey(ipayPublicKey);
            return this;
        }

        public Builder charset(String charset) {
            client.setCharset(charset);
            return this;
        }

        public Builder connectTimeout(int connectTimeout) {
            client.setConnectTimeout(connectTimeout);
            return this;
        }

        public Builder readTimeout(int readTimeout) {
            client.setReadTimeout(readTimeout);
            return this;
        }

        public Builder proxyHost(String proxyHost) {
            client.setProxyHost(proxyHost);
            return this;
        }

        public Builder proxyPort(int proxyPort) {
            client.setProxyPort(proxyPort);
            return this;
        }
    }
}
