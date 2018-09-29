package com.ipay.api.common.internal.parser.json;

import com.ipay.api.common.IpayApiException;
import com.ipay.api.common.IpayParser;
import com.ipay.api.common.request.IpayRequest;
import com.ipay.api.common.response.IpayResponse;
import com.ipay.api.common.Decryptor;
import com.ipay.api.common.SignItem;
import com.ipay.api.common.internal.mapping.Converter;

/**
 * 单个JSON对象解释器。
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class ObjectJsonParser<T extends IpayResponse> implements IpayParser<T> {

    private Class<T> clazz;

    public ObjectJsonParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T parse(String rsp) throws IpayApiException {
        Converter converter = new JsonConverter();
        return converter.toResponse(rsp, clazz);
    }

    public Class<T> getResponseClass() {
        return clazz;
    }

    public SignItem getSignItem(IpayRequest<?> request, String responseBody)
                                                                              throws IpayApiException {

        Converter converter = new JsonConverter();

        return converter.getSignItem(request, responseBody);
    }

    public String decryptSourceData(IpayRequest<?> request, String body, String format,
                                    Decryptor decryptor, String encryptType, String charset)
            throws IpayApiException {

        Converter converter = new JsonConverter();

        return converter.decryptSourceData(request, body, format, decryptor, encryptType, charset);
    }

}
