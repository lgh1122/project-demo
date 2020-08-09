package com.lgh.wechatpay.wechatpay;

import com.lgh.wechatpay.wechatpay.dto.UnifiedPayRequest;
import com.lgh.wechatpay.wechatpay.dto.UnifiedPayResponse;
import com.lgh.wechatpay.wechatpay.feign.WeChatFeign;
import com.lgh.wechatpay.wechatpay.service.WeChatOrderServices;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WechatPayApplicationTests {


    @Resource
    private WeChatFeign weChatFeign;

    @Autowired
    public WeChatOrderServices weChatOrderServices;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testPay() {
        weChatOrderServices = new WeChatOrderServices();
        weChatOrderServices.unifiedCreateOrder();
    }

    @Test
    public void testPay2() {
        weChatOrderServices = new WeChatOrderServices();
        final UnifiedPayResponse unifiedPayResponse = weChatOrderServices.unifiedCreateOrderLocal();
    }

    @Test
    public void testUnifiedOrderCreate() {

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
//        WeChatFeignConfig weChatFeignConfig = new WeChatFeignConfig();
//        WeChatPayService weChatClient2 = Feign.builder()
//                .decoder(weChatFeignConfig.feignDecoder())
//                .encoder(weChatFeignConfig.feignEncoder())
//                .contract(new feign.Contract.Default())//申明使用原生
//                .logger(new Slf4jLogger())
//                .target(WeChatPayService.class, "https://api.mch.weixin.qq.com");

//        UnifiedOrderResponse unifiedOrderResponse2 = weChatClient2.unifiedOrderCreate(unifiedPayRequest);
        UnifiedPayResponse unifiedOrderResponse = weChatFeign.unifiedOrderLocal(unifiedPayRequest);
        System.out.println("----------------");
    }

}
