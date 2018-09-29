package com.ipay.api.common.enums;

/**
 * @Author alpha.xu
 * @Date 2018/9/27 12:06
 */

public enum CommonReturnCodeEnum {
    SUCCESS("100", "调用成功", ""),
    UNKNOW_ERROR("200", "服务不可用", "服务暂不可用"),
    INVALID_AUTH_TOKEN("210", "授权权限不足", "无效的访问令牌"),
    AUTH_TOKEN_TIME_OUT("211", "授权权限不足", "访问令牌已过期"),
    INVALID_APP_AUTH_TOKEN("212", "授权权限不足", "无效的应用授权令牌"),
    INVALID_APP_AUTH_TOKEN_NO_API("213", "授权权限不足", "合作方未授权当前接口"),
    APP_AUTH_TOKEN_TIME_OUT("214", "授权权限不足", "应用授权令牌已过期"),
    MISSING_METHOD("300", "缺少必选参数", "缺少方法名参数"),
    MISSING_SIGNATURE("301", "缺少必选参数", "缺少签名参数"),
    MISSING_SIGNATURE_KEY("302", "缺少必选参数", "缺少签名配置"),
    MISSING_APP_ID("303", "缺少必选参数", "缺少appId参数"),
    MISSING_VERSION("304", "缺少必选参数", "缺少版本参数"),
    MISSING_KEY("305", "缺少必选参数", "缺少密钥版本"),
    INVALID_PARAMETER("330", "非法的参数", "参数无效"),
    INVALID_METHOD("331", "非法的参数", "不存在的方法名"),
    INVALID_SIGNATURE("332", "非法的参数", "无效签名"),
    INVALID_ENCRYPT("333", "非法的参数", "解密异常"),
    INVALID_APP_ID("334", "非法的参数", "无效的appId参数"),
    INVALID_DIGEST("335", "非法的参数", "摘要错误"),
    DECRYPTION_ERROR_NOT_VALID_ENCRYPT("336", "非法的参数", "解密出错,未配置加密密钥或加密密钥格式错误"),
    DECRYPTION_ERROR_UNKNOWN("337", "非法的参数", "解密出错，未知异常"),
    MISSING_SIGNATURE_CONFIG("338", "非法的参数", "验签出错,未配置对应签名算法的公钥或者证书");


    CommonReturnCodeEnum(String code, String msg, String submsg) {
        this.code = code;
        this.msg = msg;
        this.submsg = submsg;
    }

    String code;
    String msg;
    String submsg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getSubmsg() {
        return submsg;
    }
}
