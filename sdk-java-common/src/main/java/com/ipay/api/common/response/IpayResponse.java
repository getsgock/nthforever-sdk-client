package com.ipay.api.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.ipay.api.common.internal.mapping.ApiField;
import com.ipay.api.common.internal.util.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * API基础响应信息。
 *
 * @author fengsheng
 */
public abstract class IpayResponse implements Serializable {

    private static final long   serialVersionUID = 5014379068811962022L;

    @ApiField("code")
    @JSONField(name = "code")
    private String              code;

    @ApiField("msg")
    @JSONField(name = "msg")
    private String              msg;

    @ApiField("sub_code")
    @JSONField(name = "sub_code")
    private String              subCode;

    @ApiField("sub_msg")
    @JSONField(name = "sub_msg")
    private String              subMsg;

    private String              body;
    private Map<String, String> params;

    /**
     * Getter method for property <tt>code</tt>.
     *
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter method for property <tt>code</tt>.
     *
     * @param code value to be assigned to property code
     */
    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return this.subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return this.subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public boolean isSuccess() {
        return StringUtils.isEmpty(subCode);
    }

    @Override
    public String toString() {
        return "IpayResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", subCode='" + subCode + '\'' +
                ", subMsg='" + subMsg + '\'' +
                '}';
    }
}
