package com.lgh.wechatpay.wechatpay.service;

import com.lgh.wechatpay.wechatpay.config.FeignDecoder;
import com.lgh.wechatpay.wechatpay.config.FeignEncoder;
import com.lgh.wechatpay.wechatpay.config.FeignLocalDecoder;
import com.lgh.wechatpay.wechatpay.config.FeignLocalEncoder;
import com.lgh.wechatpay.wechatpay.dto.OrderReturnInfoDto;
import com.lgh.wechatpay.wechatpay.dto.UnifiedOrderDto;
import com.lgh.wechatpay.wechatpay.dto.UnifiedPayRequest;
import com.lgh.wechatpay.wechatpay.dto.UnifiedPayResponse;
import com.lgh.wechatpay.wechatpay.feign.WeChatClient;
import com.lgh.wechatpay.wechatpay.utils.SignUtil;
import feign.Feign;
import feign.jaxb.JAXBContextFactory;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class WeChatOrderServices {


    public OrderReturnInfoDto unifiedCreateOrder() {
        Long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date expire = new Date(currentTimeMillis + 86400000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowStr = formatter.format(now);
        String expireStr = formatter.format(expire);
        //        String nonce_str = RandomString.make(32);

        UnifiedOrderDto unifiedOrderDto = UnifiedOrderDto.builder()
                .body("店铺名-商品类名")//reqData.getStoreName() + "-" + reqData.getProductGroup()
                .mch_id("商户号")
                .appid("appid")
                .nonce_str("USUSERAP201909271218201876000001")
                .notify_url("https://local.laiyue33.online:2008/lgh/api")
                .spbill_create_ip("192.168.0.1")
                .out_trade_no("ORLOGIAP202006220110203418000001")
                .total_fee(1L)
                .trade_type("JSAPI")
                .openid("支付方openID")
                .time_start(nowStr)
                .time_expire(expireStr)
                .sign_type("MD5")
                .build();
        SignUtil.sign(unifiedOrderDto, "商户key");
        JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
                .build();

        WeChatClient weChatClient = Feign.builder()
                .decoder(new FeignDecoder(jaxbFactory))
                .encoder(new FeignEncoder(jaxbFactory))
                .contract(new feign.Contract.Default())//申明使用原生
                .logger(new Slf4jLogger())
                .target(WeChatClient.class, "https://api.mch.weixin.qq.com");


        return weChatClient.unifiedOrder(unifiedOrderDto);
    }

    public UnifiedPayResponse unifiedCreateOrderLocal() {
        Long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date expire = new Date(currentTimeMillis + 86400000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowStr = formatter.format(now);
        String expireStr = formatter.format(expire);
//        String nonce_str = RandomString.make(32);

        UnifiedPayRequest unifiedPayRequest = UnifiedPayRequest.builder()
                .mchId("1230000109")
                .appid("wxd678efh567hg6787")
                .deviceInfo("013467007045764")
                .nonceStr("5K8264ILTKCH16CQ2502SI8ZNMTM67VS")
                .notifyUrl("https://local.laiyue33.online:2008/lgh/api")
                .signType("MD5")
                .body("腾讯充值中心-QQ会员充值")
                .detail("商品详细描述，对于使用单品优惠的商户，该字段必须按照规范上传，详见")
                .attach("附加数据")
                .spbillCreateIp("192.168.0.1")
                .outTradeNo("ORLOGIAP202006220110203418000001")
                .feeType("CNY")
                .totalFee(1L)
                .tradeType("NATIVE")
                .productId("1223541321407035645")
                .limitPay("no_credit")
                //.goodsTag("")
//                .openid("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o")
                .timeStart(nowStr)
                .timeExpire(expireStr)
                .build();

        WeChatClient weChatClient2 = Feign.builder()
                .decoder(new FeignLocalDecoder())
                .encoder(new FeignLocalEncoder())
                .contract(new feign.Contract.Default())//申明使用原生
                .logger(new Slf4jLogger())
                .target(WeChatClient.class, "https://api.mch.weixin.qq.com");

        UnifiedPayResponse unifiedPayResponse = weChatClient2.unifiedOrderLocal(unifiedPayRequest);
        return unifiedPayResponse;
    }


}
