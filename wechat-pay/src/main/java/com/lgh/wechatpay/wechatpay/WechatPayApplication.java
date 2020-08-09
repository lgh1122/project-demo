package com.lgh.wechatpay.wechatpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WechatPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatPayApplication.class, args);
    }

}
