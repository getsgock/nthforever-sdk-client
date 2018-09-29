/**
 * Ipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.ipay.api.common;

import com.ipay.api.common.request.IpayRequest;
import com.ipay.api.common.response.IpayResponse;

import java.util.Map;

/**
 * @author runzhi
 */
public interface IpayClient {

    /**
     * @param <T>
     * @param request
     * @return
     * @throws IpayApiException
     */
    public <T extends IpayResponse> T execute(IpayRequest<T> request) throws IpayApiException;

    /**
     * @param <T>
     * @param request
     * @param accessToken
     * @return
     * @throws IpayApiException
     */
    public <T extends IpayResponse> T execute(IpayRequest<T> request,
                                              String authToken) throws IpayApiException;

    /**
     * @param request
     * @param accessToken
     * @param appAuthToken
     * @return
     * @throws IpayApiException
     */
    public <T extends IpayResponse> T execute(IpayRequest<T> request, String accessToken,
                                              String appAuthToken) throws IpayApiException;

    /**
     * @param <T>
     * @param request
     * @return
     * @throws IpayApiException
     */
    public <T extends IpayResponse> T pageExecute(IpayRequest<T> request) throws IpayApiException;

    /**
     * SDK客户端调用生成sdk字符串
     *
     * @param <T>
     * @param request
     * @return
     * @throws IpayApiException
     */
    public <T extends IpayResponse> T sdkExecute(IpayRequest<T> request) throws IpayApiException;

    /**
     * @param request
     * @return
     * @throws IpayApiException
     */
    public <T extends IpayResponse> T pageExecute(IpayRequest<T> request,
                                                  String method) throws IpayApiException;

    /**
     * 移动客户端同步结果返回解析的参考工具方法
     *
     * @param result       移动客户端SDK同步返回的结果map，一般包含resultStatus，result和memo三个key
     * @param requsetClazz 接口请求request类，如App支付传入 IpayTradeAppPayRequest.class
     * @return 同步返回结果的response对象
     * @throws IpayApiException
     */
    public <TR extends IpayResponse, T extends IpayRequest<TR>> TR parseAppSyncResult(Map<String, String> result,
                                                                                      Class<T> requsetClazz) throws IpayApiException;
}
