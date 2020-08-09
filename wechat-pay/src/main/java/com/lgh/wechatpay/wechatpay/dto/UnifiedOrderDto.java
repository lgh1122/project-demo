package com.lgh.wechatpay.wechatpay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by zrc on 2020-05-18.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UnifiedOrderDto {
    private String appid;// 小程序ID
    private String mch_id;// 商户号
    private String nonce_str;// 随机字符串
    private String sign_type;//签名类型
    private String sign;// 签名
    private String body;// 商品描述
    private String out_trade_no;// 商户订单号
    private Long total_fee;// 标价金额 ,单位为分
    private String spbill_create_ip;// 终端IP
    private String notify_url;// 通知地址
    private String trade_type;// 交易类型
    private String openid;//用户标识
    private String time_start;
    private String time_expire;

}
