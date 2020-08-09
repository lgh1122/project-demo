package com.lgh.wechatpay.wechatpay.feign;

import com.lgh.wechatpay.wechatpay.dto.OrderReturnInfoDto;
import com.lgh.wechatpay.wechatpay.dto.UnifiedOrderDto;
import com.lgh.wechatpay.wechatpay.dto.UnifiedPayRequest;
import com.lgh.wechatpay.wechatpay.dto.UnifiedPayResponse;
import feign.Headers;
import feign.RequestLine;

/**
 * Created by zrc on 2020-04-28.
 */
//这里不需要@FeignClient注解，使用的原生
public interface WeChatClient {
    @Headers({"Content-Type: text/xml;charset=utf-8"})
    @RequestLine("POST /pay/unifiedorder")
    OrderReturnInfoDto unifiedOrder(UnifiedOrderDto dto);


    @Headers({"Content-Type: text/xml;charset=utf-8"})
    @RequestLine("POST /pay/unifiedorder")
    UnifiedPayResponse unifiedOrderLocal(UnifiedPayRequest dto);
}
