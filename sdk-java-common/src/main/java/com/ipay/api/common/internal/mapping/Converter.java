package com.ipay.api.common.internal.mapping;

import com.ipay.api.common.Decryptor;
import com.ipay.api.common.IpayApiException;
import com.ipay.api.common.SignItem;
import com.ipay.api.common.request.IpayRequest;
import com.ipay.api.common.response.IpayResponse;

/**
 * 动态格式转换器。
 *
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public interface Converter {

    /**
     * 把字符串转换为响应对象。
     *
     * @param <T>   领域泛型
     * @param rsp   响应字符串
     * @param clazz 领域类型
     * @return 响应对象
     * @throws IpayApiException
     */
    public <T extends IpayResponse> T toResponse(String rsp, Class<T> clazz)
            throws IpayApiException;

    /**
     * 获取响应内的签名数据
     *
     * @param request
     * @param responseBody
     * @return
     * @throws IpayApiException
     */
    public SignItem getSignItem(IpayRequest<?> request, String responseBody)
            throws IpayApiException;

    /**
     * 获取解密后的响应内的真实内容
     *
     * @param request
     * @param body
     * @param format
     * @param decryptor
     * @param encryptType
     * @param charset
     * @return
     * @throws IpayApiException
     */
    public String decryptSourceData(IpayRequest<?> request, String body, String format,
                                    Decryptor decryptor, String encryptType, String charset)
            throws IpayApiException;

}
