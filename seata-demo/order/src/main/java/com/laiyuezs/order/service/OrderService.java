package com.laiyuezs.order.service;

import com.laiyuezs.order.dao.OrderRepository;
import com.laiyuezs.order.entity.Order;
import com.laiyuezs.order.feign.AccountFeignClient;
import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2019-04-04
 */
@Service
public class OrderService {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void create(String userId, String commodityCode, Integer count) {
        System.out.println("全局事务id ：" + RootContext.getXID());

        BigDecimal orderMoney = new BigDecimal(count).multiply(new BigDecimal(1));

        System.out.println("-------BigDecimal orderMoney--------"+orderMoney);

        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        order.setMoney(orderMoney);

        orderRepository.save(order);

        accountFeignClient.debit(userId, orderMoney);

    }

}
