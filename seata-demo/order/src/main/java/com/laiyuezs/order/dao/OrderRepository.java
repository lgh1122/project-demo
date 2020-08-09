package com.laiyuezs.order.dao;

import com.laiyuezs.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author liliang
 * @Date 2020/2/24 22:53
 * @Description
 **/

public interface OrderRepository extends JpaRepository<Order, Long> {
}
