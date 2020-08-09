package com.lgh.wechatpay.wechatpay.dto;

import lombok.Data;

/**
 * 微信统一下单
 */
@Data
public class UnifiedPayResponse {

    private String returnCode;
    private String returnMsg;
    /**
     * 业务结果 	result_code 	是 	String(16) 	SUCCESS 	SUCCESS/FAIL
     */
    private String resultCode;
    /**
     * 错误代码 	err_code 	否 	String(32) 	  	当result_code为FAIL时返回错误代码，详细参见下文错误列表
     */
    private String errorCode;
    /**
     * 错误代码描述 	err_code_des 	否 	String(128) 	  	当result_code为FAIL时返回错误描述，详细参见下文错误列表
     */
    private String errorCodeDesc;

    /**
     * 公众账号ID 	appid 	是 	String(32) 	wx8888888888888888 	调用接口提交的公众账号ID
     */
    private String appid;
    /**
     * 商户号 	mch_id 	是 	String(32) 	1900000109 	调用接口提交的商户号
     */
    private String mchId;
    /**
     * 设备号 	device_info 	否 	String(32) 	013467007045764 	自定义参数，可以为请求支付的终端设备号等
     */
    private String deviceInfo;

    /**
     * 随机字符串 	nonce_str 	是 	String(32) 	5K8264ILTKCH16CQ2502SI8ZNMTM67VS 	微信返回的随机字符串
     */
    private String nonceStr;

    /**
     * 签名 	sign 	是 	String(32) 	C380BEC2BFD727A4B6845133519F3AD6 	微信返回的签名值，详见签名算法
     */
    private String sign;
    /**
     * 预支付交易会话标识 	prepay_id 	是 	String(64) 	wx201410272009395522657a690389285100
     * 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
     */
    private String prepayId;

    /**
     * 交易类型 	trade_type 	是 	String(16)
     * JSAPI  JSAPI -JSAPI支付 NATIVE -Native支付 APP -APP支付 说明详见参数规定
     */
    private String tradeType;

    /**
     * 二维码链接 	code_url 	否 	String(64) 	weixin://wxpay/bizpayurl/up?pr=NwY5Mz9&groupid=00
     *
     * trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。
     *
     * 注意：code_url的值并非固定，使用时按照URL格式转成二维码即可
     */
    private String codeUrl;


    /**
     * 根据返回结果生成的签名 与返回的sign进行校验
     */
    private String checkSign;



}
