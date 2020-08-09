package com.laiyuezs.business.service;

import com.laiyuezs.business.feign.OrderFeignClient;
import com.laiyuezs.business.feign.StorageFeignClient;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author liliang
 * @Date 2020/2/24 21:21
 * @Description
 **/
@Service
public class BusinessService {
    private final static Logger logger = LoggerFactory.getLogger(BusinessService.class);

    @Autowired
    private StorageFeignClient storageFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    private static final String ERROR_USER_ID = "1003";

    /**
     * 减库存，下订单
     * @param userId
     * @param commodityCode
     * @param orderCount
     */
    @GlobalTransactional
    public void purchase(String userId, String commodityCode, int orderCount){
        final String xid = RootContext.getXID();
        logger.debug("全局事务id ：{}" , xid);
        // 减库存
        storageFeignClient.deduct(commodityCode,orderCount);
        logger.debug("购买下单 减库存完成  用户 {} ， 商品id {} ",userId,commodityCode );
        // 创建订单
        orderFeignClient.create(userId,commodityCode,orderCount);
        logger.debug("购买下单完成  用户 {} ， 商品id {} ",userId,commodityCode );

        if (ERROR_USER_ID.equals(userId)) {
            throw new RuntimeException("account branch exception");
        }
    }


    /**
     * 减库存，下订单
     * @param userId
     * @param commodityCode
     * @param orderCount
     */
    public void purchaseNoTransaction(String userId, String commodityCode, int orderCount){
        final String xid = RootContext.getXID();
        logger.debug("全局事务id ：{}" , xid);
        // 减库存
        storageFeignClient.deduct(commodityCode,orderCount);
        // 创建订单
        orderFeignClient.create(userId,commodityCode,orderCount);
        logger.debug("购买下单完成  用户 {} ， 商品id {} ",userId,commodityCode );

        if (ERROR_USER_ID.equals(userId)) {
            throw new RuntimeException("account branch exception");
        }
    }
}
