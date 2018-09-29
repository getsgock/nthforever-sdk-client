package com.ipay.api.common.internal.parser.json;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ipay.api.common.IpayApiException;
import com.ipay.api.common.IpayConstants;
import com.ipay.api.common.request.IpayRequest;
import com.ipay.api.common.response.IpayResponse;
import com.ipay.api.common.Decryptor;
import com.ipay.api.common.ResponseParseItem;
import com.ipay.api.common.SignItem;
import com.ipay.api.common.internal.mapping.Converter;
import com.ipay.api.common.internal.mapping.Converters;
import com.ipay.api.common.internal.mapping.Reader;
import com.ipay.api.common.internal.util.StringUtils;
import com.ipay.api.common.internal.util.json.ExceptionErrorListener;
import com.ipay.api.common.internal.util.json.JSONReader;
import com.ipay.api.common.internal.util.json.JSONValidatingReader;

/**
 * JSON格式转换器。
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class JsonConverter implements Converter {

    public <T extends IpayResponse> T toResponse(String rsp, Class<T> clazz)
                                                                              throws IpayApiException {
        JSONReader reader = new JSONValidatingReader(new ExceptionErrorListener());
        Object rootObj = reader.read(rsp);
        if (rootObj instanceof Map<?, ?>) {
            Map<?, ?> rootJson = (Map<?, ?>) rootObj;
            Collection<?> values = rootJson.values();
            for (Object rspObj : values) {
                if (rspObj instanceof Map<?, ?>) {
                    Map<?, ?> rspJson = (Map<?, ?>) rspObj;
                    return fromJson(rspJson, clazz);
                }
            }
        }
        return null;
    }

    /**
     * 把JSON格式的数据转换为对象。
     * 
     * @param <T> 泛型领域对象
     * @param json JSON格式的数据
     * @param clazz 泛型领域类型
     * @return 领域对象
     * @throws IpayApiException
     */
    public <T> T fromJson(final Map<?, ?> json, Class<T> clazz) throws IpayApiException {
        return Converters.convert(clazz, new Reader() {
            public boolean hasReturnField(Object name) {
                return json.containsKey(name);
            }

            public Object getPrimitiveObject(Object name) {
                return json.get(name);
            }

            public Object getObject(Object name, Class<?> type) throws IpayApiException {
                Object tmp = json.get(name);
                if (tmp instanceof Map<?, ?>) {
                    Map<?, ?> map = (Map<?, ?>) tmp;
                    return fromJson(map, type);
                } else {
                    return null;
                }
            }

            public List<?> getListObjects(Object listName, Object itemName, Class<?> subType)
                                                                                             throws IpayApiException {
                List<Object> listObjs = null;

                Object listTmp = json.get(listName);
                if (listTmp instanceof Map<?, ?>) {
                    Map<?, ?> jsonMap = (Map<?, ?>) listTmp;
                    Object itemTmp = jsonMap.get(itemName);
                    if (itemTmp == null && listName != null) {
                        String listNameStr = listName.toString();
                        itemTmp = jsonMap.get(listNameStr.substring(0, listNameStr.length() - 1));
                    }
                    if (itemTmp instanceof List<?>) {
                        listObjs = getListObjectsInner(subType, itemTmp);
                    }
                } else if (listTmp instanceof List<?>) {
                    listObjs = getListObjectsInner(subType, listTmp);
                }

                return listObjs;
            }

            private List<Object> getListObjectsInner(Class<?> subType, Object itemTmp)
                                                                                      throws IpayApiException {
                List<Object> listObjs;
                listObjs = new ArrayList<Object>();
                List<?> tmpList = (List<?>) itemTmp;
                for (Object subTmp : tmpList) {
                    Object obj = null;
                    if (String.class.isAssignableFrom(subType)) {
                        obj = subTmp;
                    } else if (Long.class.isAssignableFrom(subType)) {
                        obj = subTmp;
                    } else if (Integer.class.isAssignableFrom(subType)) {
                        obj = subTmp;
                    } else if (Boolean.class.isAssignableFrom(subType)) {
                        obj = subTmp;
                    } else if (Date.class.isAssignableFrom(subType)) {
                        DateFormat format = new SimpleDateFormat(IpayConstants.DATE_TIME_FORMAT);
                        try {
                            obj = format.parse(String.valueOf(subTmp));
                        } catch (ParseException e) {
                            throw new IpayApiException(e);
                        }
                    } else if (subTmp instanceof Map<?, ?>) {// object
                        Map<?, ?> subMap = (Map<?, ?>) subTmp;
                        obj = fromJson(subMap, subType);
                    }

                    if (obj != null) {
                        listObjs.add(obj);
                    }
                }
                return listObjs;
            }

        });
    }

    /** 
     * @see com.ipay.api.common.internal.mapping.Converter#getSignItem(IpayRequest, String)
     */
    public SignItem getSignItem(IpayRequest<?> request, String responseBody)
                                                                              throws IpayApiException {

        // 响应为空则直接返回
        if (StringUtils.isEmpty(responseBody)) {

            return null;
        }

        SignItem signItem = new SignItem();

        // 获取签名
        String sign = getSign(responseBody);
        signItem.setSign(sign);

        // 签名源串
        String signSourceData = getSignSourceData(request, responseBody);
        signItem.setSignSourceDate(signSourceData);

        return signItem;
    }

    /**
     * 
     * @param request
     * @param body
     * @return
     */
    private String getSignSourceData(IpayRequest<?> request, String body) {

        // 加签源串起点
        String rootNode = request.getApiMethodName().replace('.', '_')
                          + IpayConstants.RESPONSE_SUFFIX;
        String errorRootNode = IpayConstants.ERROR_RESPONSE;

        int indexOfRootNode = body.indexOf(rootNode);
        int indexOfErrorRoot = body.indexOf(errorRootNode);

        // 成功或者新版接口
        if (indexOfRootNode > 0) {

            return parseSignSourceData(body, rootNode, indexOfRootNode);

            // 老版本失败接口
        } else if (indexOfErrorRoot > 0) {

            return parseSignSourceData(body, errorRootNode, indexOfErrorRoot);
        } else {
            return null;
        }
    }

    /**
     *   获取签名源串内容
     *    
     * 
     * @param body
     * @param rootNode
     * @param indexOfRootNode
     * @return
     */
    private String parseSignSourceData(String body, String rootNode, int indexOfRootNode) {

        //  第一个字母+长度+引号和分号
        int signDataStartIndex = indexOfRootNode + rootNode.length() + 2;
        int indexOfSign = body.indexOf("\"" + IpayConstants.SIGN + "\"");

        if (indexOfSign < 0) {

            return null;
        }

        // 签名前-逗号
        int signDataEndIndex = indexOfSign - 1;

        return body.substring(signDataStartIndex, signDataEndIndex);
    }

    /**
     * 获取签名
     * 
     * @param body
     * @return
     */
    private String getSign(String body) {

        JSONReader reader = new JSONValidatingReader(new ExceptionErrorListener());
        Object rootObj = reader.read(body);
        Map<?, ?> rootJson = (Map<?, ?>) rootObj;

        return (String) rootJson.get(IpayConstants.SIGN);
    }


    public String decryptSourceData(IpayRequest<?> request, String body, String format,
                                    Decryptor decryptor, String encryptType, String charset)
                                                                                          throws IpayApiException {

        ResponseParseItem respSignSourceData = getJSONSignSourceData(request, body);

        String bodyIndexContent = body.substring(0, respSignSourceData.getStartIndex());
        String bodyEndContent = body.substring(respSignSourceData.getEndIndex());

        return bodyIndexContent
               + decryptor.decrypt(respSignSourceData.getEncryptContent(), encryptType, charset)
               + bodyEndContent;

    }

    /**
     *  获取JSON响应加签内容串
     * 
     * @param request
     * @param body
     * @return
     */
    private ResponseParseItem getJSONSignSourceData(IpayRequest<?> request, String body) {

        String rootNode = request.getApiMethodName().replace('.', '_')
                          + IpayConstants.RESPONSE_SUFFIX;
        String errorRootNode = IpayConstants.ERROR_RESPONSE;

        int indexOfRootNode = body.indexOf(rootNode);
        int indexOfErrorRoot = body.indexOf(errorRootNode);

        if (indexOfRootNode > 0) {

            return parseJSONSignSourceData(body, rootNode, indexOfRootNode);

        } else if (indexOfErrorRoot > 0) {

            return parseJSONSignSourceData(body, errorRootNode, indexOfErrorRoot);
        } else {
            return null;
        }
    }

    /**
     * 
     * 
     * @param body
     * @param rootNode
     * @param indexOfRootNode
     * @return
     */
    private ResponseParseItem parseJSONSignSourceData(String body, String rootNode,
                                                      int indexOfRootNode) {

        int signDataStartIndex = indexOfRootNode + rootNode.length() + 2;
        int indexOfSign = body.indexOf("\"" + IpayConstants.SIGN + "\"");

        if (indexOfSign < 0) {

            indexOfSign = body.length();
        }

        int signDataEndIndex = indexOfSign - 1;

        String encryptContent = body.substring(signDataStartIndex + 1, signDataEndIndex - 1);

        return new ResponseParseItem(signDataStartIndex, signDataEndIndex, encryptContent);
    }

}
