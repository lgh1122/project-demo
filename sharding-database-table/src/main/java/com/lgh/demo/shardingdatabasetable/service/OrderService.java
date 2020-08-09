package com.lgh.demo.shardingdatabasetable.service;

import com.lgh.demo.shardingdatabasetable.entity.Goods;
import com.lgh.demo.shardingdatabasetable.entity.GoodsType;
import com.lgh.demo.shardingdatabasetable.entity.Order;
import com.lgh.demo.shardingdatabasetable.entity.OrderItem;
import com.lgh.demo.shardingdatabasetable.repository.GoodsRepository;
import com.lgh.demo.shardingdatabasetable.repository.GoodsTypeRepository;
import com.lgh.demo.shardingdatabasetable.repository.OrderItemRepository;
import com.lgh.demo.shardingdatabasetable.repository.OrderRepository;
import com.lgh.demo.shardingdatabasetable.util.IdGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author 17519
 */
@Service
public class OrderService {

    @Resource
    private OrderRepository orderRepository;
    @Resource
    private OrderItemRepository orderItemRepository;

    @Resource
    private GoodsRepository goodsRepository;
    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Order createOrder(Order order) {
        final String orderId = IdGenerator.getNextId(Order.class);
        order.setId(orderId);
        order.setCreateTime(System.currentTimeMillis());
        final List<OrderItem> orderItems = order.getOrderItems();
        if (CollectionUtils.isNotEmpty(orderItems)) {
            for (OrderItem orderItem : orderItems) {
                orderItem.setOrderId(orderId);
                orderItem.setId(IdGenerator.getNextId(OrderItem.class));
                orderItemRepository.save(orderItem);
            }
        }
        order.setOrderItems(null);
        orderRepository.save(order);
        return order;
    }

    public Order getOrder(String orderId) {
        return orderRepository.getOne(orderId);
    }

    public String deleteOrder(String orderId) {
        orderRepository.deleteById(orderId);
        return "success";
    }

    public List<Order> listOrder(Order order) {
        final Order param = new Order();
        if (StringUtils.isNotBlank(order.getId())) {
            param.setId(order.getId());
        }
        if (StringUtils.isNotBlank(order.getUserId())) {
            param.setUserId(order.getUserId());
        }
        Example<Order> orderExample = Example.of(param);
        ExampleMatcher exampleMatcher = orderExample.getMatcher();
        final List<Order> all = orderRepository.findAll(orderExample);
        return all;
    }

    public List<OrderItem> listOrderItem() {
        StringBuilder dataSql = new StringBuilder();
        dataSql.append(" select i.id from t_order_item i join t_order o on i.order_id = o.order_id where o.order_id in ('DEORDEDE202003181447100751000001','DEORDEDE202003181447100751000002') ");
        final Query nativeQuery = entityManager.createNativeQuery(dataSql.toString());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(OrderItem.class));
        final List<OrderItem> resultList = nativeQuery.getResultList();

        return resultList;
    }

    public Goods createGoods(Goods goods) {
        return goodsRepository.save(goods);
    }


    public Goods getGoods(String goodsId) {
        return goodsRepository.getOne(goodsId);
    }

    public String deleteGoods(String goodsId) {
        goodsRepository.deleteById(goodsId);
        return "success";
    }

    public List<Goods> listGoods(Goods goods) {
        final Goods param = new Goods();
        if (StringUtils.isNotBlank(goods.getId())) {
            param.setId(goods.getId());
        }
        if (StringUtils.isNotBlank(goods.getTypeId())) {
            param.setTypeId(goods.getTypeId());
        }
        Example<Goods> goodsExample = Example.of(param);

        final List<Goods> all = goodsRepository.findAll(goodsExample);
        return all;
    }

    public GoodsType createGoodsType(GoodsType goodsType) {
        return goodsTypeRepository.save(goodsType);
    }

    public GoodsType getGoodsType(String goodsTypeId) {
        return goodsTypeRepository.getOne(goodsTypeId);
    }

    public String deleteGoodsType(String goodsTypeId) {
        goodsTypeRepository.deleteById(goodsTypeId);
        return "success";
    }

    public List<GoodsType> listGoodsType(GoodsType goodsType) {
        return goodsTypeRepository.findAll();
    }


}
