package com.lgh.wechatpay.wechatpay.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by zrc on 2020-05-18.
 */
@Data
@XmlRootElement(name = "xml")//必须有，没有无法转换
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderReturnInfoDto {
    private String return_code;
    private String return_msg;
    private String result_code;
    private String appid;
    private String mch_id;
    private String nonce_str;
    private String sign;
    private String prepay_id;
    private String trade_type;

}
