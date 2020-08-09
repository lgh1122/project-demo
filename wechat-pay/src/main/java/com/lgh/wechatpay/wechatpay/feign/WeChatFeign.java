package com.lgh.wechatpay.wechatpay.feign;

import com.lgh.wechatpay.wechatpay.config.WeChatFeignConfig;
import com.lgh.wechatpay.wechatpay.dto.UnifiedPayRequest;
import com.lgh.wechatpay.wechatpay.dto.UnifiedPayResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zrc on 2020-04-28.
 */
//这里不需要@FeignClient注解，使用的原生
@FeignClient(name = "weChatFeign", url = "${ly.wechat.pay-host:https://api.mch.weixin.qq.com}", configuration = WeChatFeignConfig.class)
public interface WeChatFeign {
//    @Headers({"Content-Type: text/xml;charset=utf-8"})
//    @RequestLine("POST /pay/unifiedorder")
//    OrderReturnInfoDto unifiedOrder(UnifiedOrderDto dto);


    @PostMapping(value = "/pay/unifiedorder")
    @ResponseBody
    UnifiedPayResponse unifiedOrderLocal(UnifiedPayRequest dto);
}
