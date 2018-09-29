package com.ipay.api.common.internal.parser.xml;

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
public class ObjectXmlParser<T extends IpayResponse> implements IpayParser<T> {

    private Class<T> clazz;

    public ObjectXmlParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T parse(String rsp) throws IpayApiException {
        Converter converter = new XmlConverter();
        return converter.toResponse(rsp, clazz);
    }

    public Class<T> getResponseClass() {
        return clazz;
    }

    public SignItem getSignItem(IpayRequest<?> request, String responseBody)
                                                                              throws IpayApiException {

        Converter converter = new XmlConverter();

        return converter.getSignItem(request, responseBody);
    }

    public String decryptSourceData(IpayRequest<?> request, String body, String format,
                                    Decryptor decryptor, String encryptType, String charset)
            throws IpayApiException {

        Converter converter = new XmlConverter();

        return converter.decryptSourceData(request, body, format, decryptor, encryptType, charset);
    }

}
