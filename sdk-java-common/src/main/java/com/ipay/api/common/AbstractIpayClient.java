/**
 * Ipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.ipay.api.common;

import com.ipay.api.common.internal.parser.json.ObjectJsonParser;
import com.ipay.api.common.internal.parser.xml.ObjectXmlParser;
import com.ipay.api.common.internal.util.*;
import com.ipay.api.common.internal.util.json.JSONWriter;
import com.ipay.api.common.request.IpayRequest;
import com.ipay.api.common.response.IpayResponse;

import java.io.IOException;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author liuqun.lq
 * @version $Id: AbstractIpayClient.java, v 0.1 2018-07-03 10:45:21 liuqun.lq Exp $
 */
public abstract class AbstractIpayClient implements IpayClient {

    private String serverUrl;
    private String appId;
    private String prodCode;
    private String format = IpayConstants.FORMAT_JSON;
    private String signType = IpayConstants.SIGN_TYPE_RSA;
    private String encryptType = IpayConstants.ENCRYPT_TYPE_AES;

    private String charset;

    private int connectTimeout = 3000;
    private int readTimeout = 15000;

    private String proxyHost;
    private int proxyPort;

    static {
        //�����ȫ����
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");
    }

    public AbstractIpayClient(String serverUrl, String appId, String format,
                              String charset, String signType) {
        this.serverUrl = serverUrl;
        this.appId = appId;
        if (!StringUtils.isEmpty(format)) {
            this.format = format;
        }
        this.charset = charset;
        if (!StringUtils.isEmpty(signType)) {
            this.signType = signType;
        }
    }

    public AbstractIpayClient(String serverUrl, String appId, String format,
                              String charset, String signType, String proxyHost,
                              int proxyPort) {
        this(serverUrl, appId, format, charset, signType);
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    public AbstractIpayClient(String serverUrl, String appId, String format,
                              String charset, String signType, String encryptType) {
        this(serverUrl, appId, format, charset, signType);
        if (!StringUtils.isEmpty(encryptType)) {
            this.encryptType = encryptType;
        }
    }

    public <T extends IpayResponse> T execute(IpayRequest<T> request) throws IpayApiException {
        return execute(request, null);
    }

    public <T extends IpayResponse> T execute(IpayRequest<T> request,
                                              String accessToken) throws IpayApiException {

        return execute(request, accessToken, null);
    }

    public <T extends IpayResponse> T execute(IpayRequest<T> request, String accessToken,
                                              String appAuthToken) throws IpayApiException {

        IpayParser<T> parser = null;
        if (IpayConstants.FORMAT_XML.equals(this.format)) {
            parser = new ObjectXmlParser<T>(request.getResponseClass());
        } else {
            parser = new ObjectJsonParser<T>(request.getResponseClass());
        }

        return _execute(request, parser, accessToken, appAuthToken);
    }

    public <T extends IpayResponse> T pageExecute(IpayRequest<T> request) throws IpayApiException {
        return pageExecute(request, "POST");
    }

    public <T extends IpayResponse> T pageExecute(IpayRequest<T> request,
                                                  String httpMethod) throws IpayApiException {
        RequestParametersHolder requestHolder = getRequestHolderWithSign(request, null, null);
        // ��ӡ����������
        if (IpayLogger.isBizDebugEnabled()) {
            IpayLogger.logBizDebug(getRedirectUrl(requestHolder));
        }
        T rsp = null;
        try {
            Class<T> clazz = request.getResponseClass();
            rsp = clazz.newInstance();
        } catch (InstantiationException e) {
            IpayLogger.logBizError(e);
        } catch (IllegalAccessException e) {
            IpayLogger.logBizError(e);
        }
        if ("GET".equalsIgnoreCase(httpMethod)) {
            rsp.setBody(getRedirectUrl(requestHolder));
        } else {
            String baseUrl = getRequestUrl(requestHolder);
            rsp.setBody(WebUtils.buildForm(baseUrl, requestHolder.getApplicationParams()));
        }
        return rsp;
    }

    public <T extends IpayResponse> T sdkExecute(IpayRequest<T> request) throws IpayApiException {
        RequestParametersHolder requestHolder = getRequestHolderWithSign(request, null, null);
        // ��ӡ����������
        if (IpayLogger.isBizDebugEnabled()) {
            IpayLogger.logBizDebug(getSdkParams(requestHolder));
        }
        T rsp = null;
        try {
            Class<T> clazz = request.getResponseClass();
            rsp = clazz.newInstance();
        } catch (InstantiationException e) {
            IpayLogger.logBizError(e);
        } catch (IllegalAccessException e) {
            IpayLogger.logBizError(e);
        }
        rsp.setBody(getSdkParams(requestHolder));
        return rsp;
    }

    public <TR extends IpayResponse, T extends IpayRequest<TR>> TR parseAppSyncResult(Map<String, String> result,
                                                                                      Class<T> requsetClazz) throws IpayApiException {
        TR tRsp = null;
        String rsp = result.get("result");

        try {
            T request = requsetClazz.newInstance();
            Class<TR> responseClazz = request.getResponseClass();

            //resultΪ��ֱ�ӷ���SYSTEM_ERROR
            if (StringUtils.isEmpty(rsp)) {
                tRsp = responseClazz.newInstance();
                tRsp.setCode("20000");
                tRsp.setSubMsg(result.get("memo"));
            } else {
                IpayParser<TR> parser = null;
                if (IpayConstants.FORMAT_XML.equals(this.format)) {
                    parser = new ObjectXmlParser<TR>(responseClazz);
                } else {
                    parser = new ObjectJsonParser<TR>(responseClazz);
                }

                // ����ʵ�ʴ�
                tRsp = parser.parse(rsp);
                tRsp.setBody(rsp);

                // ��ǩ�Ƕ����󷵻�ԭʼ��
                checkResponseSign(request, parser, rsp, tRsp.isSuccess());
                if (!tRsp.isSuccess()) {
                    IpayLogger.logBizError(rsp);
                }
            }
        } catch (RuntimeException e) {
            IpayLogger.logBizError(rsp);
            throw e;
        } catch (IpayApiException e) {
            IpayLogger.logBizError(rsp);
            throw new IpayApiException(e);
        } catch (InstantiationException e) {
            IpayLogger.logBizError(rsp);
            throw new IpayApiException(e);
        } catch (IllegalAccessException e) {
            IpayLogger.logBizError(rsp);
            throw new IpayApiException(e);
        }
        return tRsp;
    }

    /**
     * ��װ�ӿڲ�����������ܡ�ǩ���߼�
     *
     * @param request
     * @param accessToken
     * @param appAuthToken
     * @return
     * @throws IpayApiException
     */
    private <T extends IpayResponse> RequestParametersHolder getRequestHolderWithSign(IpayRequest<?> request,
                                                                                      String accessToken,
                                                                                      String appAuthToken) throws IpayApiException {
        RequestParametersHolder requestHolder = new RequestParametersHolder();
        IpayHashMap appParams = new IpayHashMap(request.getTextParams());

        // ����API����biz_content������ֵΪ��ʱ�����л�bizModel���bizContent
        try {
            if (request.getClass().getMethod("getBizContent") != null
                    && StringUtils.isEmpty(appParams.get(IpayConstants.BIZ_CONTENT_KEY))
                    && request.getBizModel() != null) {
                appParams.put(IpayConstants.BIZ_CONTENT_KEY,
                        new JSONWriter().write(request.getBizModel(), true));
            }
        } catch (NoSuchMethodException e) {
            // �Ҳ���getBizContent��ʲô������
        } catch (SecurityException e) {
            IpayLogger.logBizError(e);
        }

        // ֻ���½ӿں�������Կ����֧�ּ���
        if (request.isNeedEncrypt()) {

            if (StringUtils.isEmpty(appParams.get(IpayConstants.BIZ_CONTENT_KEY))) {

                throw new IpayApiException("��ǰAPI��֧�ּ�������");
            }

            // ��Ҫ���ܱ���������Կ�ͼ����㷨
            if (StringUtils.isEmpty(this.encryptType) || getEncryptor() == null) {

                throw new IpayApiException("API����Ҫ����ܣ������������Կ���ͺͼ�����");
            }

            String encryptContent = getEncryptor().encrypt(
                    appParams.get(IpayConstants.BIZ_CONTENT_KEY), this.encryptType, this.charset);

            appParams.put(IpayConstants.BIZ_CONTENT_KEY, encryptContent);
        }

        if (!StringUtils.isEmpty(appAuthToken)) {
            appParams.put(IpayConstants.APP_AUTH_TOKEN, appAuthToken);
        }

        requestHolder.setApplicationParams(appParams);

        if (StringUtils.isEmpty(charset)) {
            charset = IpayConstants.CHARSET_UTF8;
        }

        IpayHashMap protocalMustParams = new IpayHashMap();
        protocalMustParams.put(IpayConstants.METHOD, request.getApiMethodName());
        protocalMustParams.put(IpayConstants.APP_ID, this.appId);
        protocalMustParams.put(IpayConstants.SIGN_TYPE, this.signType);
        protocalMustParams.put(IpayConstants.RETURN_URL, request.getReturnUrl());
        protocalMustParams.put(IpayConstants.CHARSET, charset);

        if (request.isNeedEncrypt()) {
            protocalMustParams.put(IpayConstants.ENCRYPT_TYPE, this.encryptType);
        }

        Long timestamp = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat(IpayConstants.DATE_TIME_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone(IpayConstants.DATE_TIMEZONE));
        protocalMustParams.put(IpayConstants.TIMESTAMP, df.format(new Date(timestamp)));
        requestHolder.setProtocalMustParams(protocalMustParams);

        IpayHashMap protocalOptParams = new IpayHashMap();
        protocalOptParams.put(IpayConstants.FORMAT, format);
        protocalOptParams.put(IpayConstants.ACCESS_TOKEN, accessToken);
        protocalOptParams.put(IpayConstants.ALIPAY_SDK, IpayConstants.SDK_VERSION);
        protocalOptParams.put(IpayConstants.PROD_CODE, request.getProdCode());
        requestHolder.setProtocalOptParams(protocalOptParams);

        if (!StringUtils.isEmpty(this.signType)) {

            String signContent = IpaySignature.getSignatureContent(requestHolder);
            protocalMustParams.put(IpayConstants.SIGN,
                    getSigner().sign(signContent, this.signType, charset));

        } else {
            protocalMustParams.put(IpayConstants.SIGN, "");
        }
        return requestHolder;
    }

    /**
     * ��ȡPOST�����base url
     *
     * @param requestHolder
     * @return
     * @throws IpayApiException
     */
    private String getRequestUrl(RequestParametersHolder requestHolder) throws IpayApiException {
        StringBuffer urlSb = new StringBuffer(serverUrl);
        try {
            String sysMustQuery = WebUtils.buildQuery(requestHolder.getProtocalMustParams(),
                    charset);
            String sysOptQuery = WebUtils.buildQuery(requestHolder.getProtocalOptParams(), charset);
            String method = requestHolder.getProtocalMustParams().get(IpayConstants.METHOD);
            method = method.replace(".", "/");
            urlSb.append(method);
            urlSb.append("?");
            urlSb.append(sysMustQuery);
            if (sysOptQuery != null & sysOptQuery.length() > 0) {
                urlSb.append("&");
                urlSb.append(sysOptQuery);
            }
        } catch (IOException e) {
            throw new IpayApiException(e);
        }

        return urlSb.toString();
    }

    /**
     * GETģʽ�»�ȡ��ת����
     *
     * @param requestHolder
     * @return
     * @throws IpayApiException
     */
    private String getRedirectUrl(RequestParametersHolder requestHolder) throws IpayApiException {
        StringBuffer urlSb = new StringBuffer(serverUrl);
        try {
            Map<String, String> sortedMap = IpaySignature.getSortedMap(requestHolder);
            String sortedQuery = WebUtils.buildQuery(sortedMap, charset);
            urlSb.append("?");
            urlSb.append(sortedQuery);
        } catch (IOException e) {
            throw new IpayApiException(e);
        }

        return urlSb.toString();
    }

    /**
     * ƴװsdk����ʱ��������
     *
     * @param requestHolder
     * @return
     * @throws IpayApiException
     */
    private String getSdkParams(RequestParametersHolder requestHolder) throws IpayApiException {
        StringBuffer urlSb = new StringBuffer();
        try {
            Map<String, String> sortedMap = IpaySignature.getSortedMap(requestHolder);
            String sortedQuery = WebUtils.buildQuery(sortedMap, charset);
            urlSb.append(sortedQuery);
        } catch (IOException e) {
            throw new IpayApiException(e);
        }

        return urlSb.toString();
    }

    private <T extends IpayResponse> T _execute(IpayRequest<T> request, IpayParser<T> parser,
                                                String authToken,
                                                String appAuthToken) throws IpayApiException {

        long beginTime = System.currentTimeMillis();
        Map<String, Object> rt = doPost(request, authToken, appAuthToken);
        if (rt == null) {
            return null;
        }
        Map<String, Long> costTimeMap = new HashMap<String, Long>();
        if (rt.containsKey("prepareTime")) {
            costTimeMap.put("prepareCostTime", (Long) (rt.get("prepareTime")) - beginTime);
            if (rt.containsKey("requestTime")) {
                costTimeMap.put("requestCostTime", (Long) (rt.get("requestTime")) - (Long) (rt.get("prepareTime")));
            }
        }


        T tRsp = null;

        try {

            // ����Ҫ�������Ƚ���
            ResponseEncryptItem responseItem = decryptResponse(request, rt, parser);

            // ����ʵ�ʴ�
            tRsp = parser.parse(responseItem.getRealContent());
            tRsp.setBody(responseItem.getRealContent());

            // ��ǩ�Ƕ����󷵻�ԭʼ��
            checkResponseSign(request, parser, responseItem.getRespContent(), tRsp.isSuccess());

            if (costTimeMap.containsKey("requestCostTime")) {
                costTimeMap.put("postCostTime", System.currentTimeMillis() - (Long) (rt.get("requestTime")));
            }
        } catch (RuntimeException e) {

            IpayLogger.logBizError((String) rt.get("rsp"), costTimeMap);
            throw e;
        } catch (IpayApiException e) {

            IpayLogger.logBizError((String) rt.get("rsp"), costTimeMap);
            throw new IpayApiException(e);
        }

        tRsp.setParams((IpayHashMap) rt.get("textParams"));
        if (!tRsp.isSuccess()) {
            IpayLogger.logErrorScene(rt, tRsp, "", costTimeMap);
        } else {
            IpayLogger.logBizSummary(rt, tRsp, costTimeMap);
        }
        return tRsp;
    }

    /**
     * @param request
     * @param accessToken
     * @param appAuthToken
     * @return
     * @throws IpayApiException
     */
    private <T extends IpayResponse> Map<String, Object> doPost(IpayRequest<T> request,
                                                                String accessToken,
                                                                String appAuthToken) throws IpayApiException {
        Map<String, Object> result = new HashMap<String, Object>();
        RequestParametersHolder requestHolder = getRequestHolderWithSign(request, accessToken,
                appAuthToken);

        String url = getRequestUrl(requestHolder);

        // ��ӡ����������
        if (IpayLogger.isBizDebugEnabled()) {
            IpayLogger.logBizDebug(getRedirectUrl(requestHolder));
        }
        result.put("prepareTime", System.currentTimeMillis());

        String rsp = null;
        try {
            if (request instanceof IpayUploadRequest) {
                IpayUploadRequest<T> uRequest = (IpayUploadRequest<T>) request;
                Map<String, FileItem> fileParams = IpayUtils.cleanupMap(uRequest.getFileParams());
                rsp = WebUtils.doPost(url, requestHolder.getApplicationParams(), fileParams,
                        charset, connectTimeout, readTimeout, proxyHost, proxyPort);
            } else {
                rsp = WebUtils.doPost(url, requestHolder.getApplicationParams(), charset,
                        connectTimeout, readTimeout, proxyHost, proxyPort);
            }
        } catch (IOException e) {
            throw new IpayApiException(e);
        }
        result.put("requestTime", System.currentTimeMillis());
        result.put("rsp", rsp);
        result.put("textParams", requestHolder.getApplicationParams());
        result.put("protocalMustParams", requestHolder.getProtocalMustParams());
        result.put("protocalOptParams", requestHolder.getProtocalOptParams());
        result.put("url", url);
        return result;
    }

    /**
     * �����Ӧǩ��
     *
     * @param request
     * @param parser
     * @param responseBody
     * @param responseIsSucess
     * @throws IpayApiException
     */
    private <T extends IpayResponse> void checkResponseSign(IpayRequest<T> request,
                                                            IpayParser<T> parser,
                                                            String responseBody,
                                                            boolean responseIsSucess) throws IpayApiException {
        // ��Գɹ��������֧������Կ�Ľ�����ǩ
        if (getSignChecker() != null) {

            SignItem signItem = parser.getSignItem(request, responseBody);

            if (signItem == null) {

                throw new IpayApiException("sign check fail: Body is Empty!");
            }

            if (responseIsSucess
                    || (!responseIsSucess && !StringUtils.isEmpty(signItem.getSign()))) {

                boolean rsaCheckContent = getSignChecker().check(signItem.getSignSourceDate(),
                        signItem.getSign(), this.signType, this.charset);

                if (!rsaCheckContent) {

                    // ���JSON \/���⣬�滻/���ٳ�����һ����֤
                    if (!StringUtils.isEmpty(signItem.getSignSourceDate())
                            && signItem.getSignSourceDate().contains("\\/")) {

                        String srouceData = signItem.getSignSourceDate().replace("\\/", "/");

                        boolean jsonCheck = getSignChecker().check(srouceData, signItem.getSign(),
                                this.signType, this.charset);

                        if (!jsonCheck) {
                            throw new IpayApiException(
                                    "sign check fail: check Sign and Data Fail��JSON also��");
                        }
                    } else {

                        throw new IpayApiException("sign check fail: check Sign and Data Fail!");
                    }
                }
            }

        }
    }

    /**
     * ������Ӧ
     *
     * @param request
     * @param rt
     * @param parser
     * @return
     * @throws IpayApiException
     */
    private <T extends IpayResponse> ResponseEncryptItem decryptResponse(IpayRequest<T> request,
                                                                         Map<String, Object> rt,
                                                                         IpayParser<T> parser) throws IpayApiException {

        String responseBody = (String) rt.get("rsp");

        String realBody = null;

        // ����
        if (request.isNeedEncrypt()) {

            // ����ԭʼ��
            realBody = parser.decryptSourceData(request, responseBody, this.format,
                    getDecryptor(), this.encryptType, this.charset);
        } else {

            // ����ԭ���ݴ�
            realBody = (String) rt.get("rsp");
        }

        return new ResponseEncryptItem(responseBody, realBody);

    }

    void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    void setAppId(String appId) {
        this.appId = appId;
    }

    void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    void setFormat(String format) {
        this.format = format;
    }

    void setSignType(String signType) {
        this.signType = signType;
    }

    void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
    }

    void setCharset(String charset) {
        this.charset = charset;
    }

    void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public abstract Signer getSigner();

    public abstract SignChecker getSignChecker();

    public abstract Encryptor getEncryptor();

    public abstract Decryptor getDecryptor();
}
