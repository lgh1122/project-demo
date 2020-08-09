package com.lgh.wechatpay.wechatpay.controller;

import com.lgh.wechatpay.wechatpay.utils.SignUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/wx")
public class WeChatOrderController {

    @PostMapping(value = "/pay/callback")
    public void wxProPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String xmlMsg = SignUtil.readData(request);
        Map<String, String> resultMap = SignUtil.xmlToMap(xmlMsg);
        if (resultMap.get("return_code").equals("SUCCESS")) {

            Boolean isSignatureValid = SignUtil.isSignatureValid(resultMap, "商户key");
            if (isSignatureValid) {//安全起见校验
                String orderNo = resultMap.get("out_trade_no");
                //更新订单信息
            }

        }
        String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
