package com.laiyuezs.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Date 2020/2/24 21:26
 * @Description
 **/
@FeignClient(name = "order-service")
public interface OrderFeignClient {

    @GetMapping("/create")
    void create(@RequestParam("userId") String userId, @RequestParam("commodityCode") String commodityCode, @RequestParam("count") Integer count);
}
