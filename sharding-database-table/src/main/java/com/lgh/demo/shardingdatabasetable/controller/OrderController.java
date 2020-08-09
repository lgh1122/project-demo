package com.lgh.demo.shardingdatabasetable.controller;

import com.lgh.demo.shardingdatabasetable.entity.Goods;
import com.lgh.demo.shardingdatabasetable.entity.GoodsType;
import com.lgh.demo.shardingdatabasetable.entity.Order;
import com.lgh.demo.shardingdatabasetable.entity.OrderItem;
import com.lgh.demo.shardingdatabasetable.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 17519
 */
@RestController()
@RequestMapping()
public class OrderController {

    @Autowired
    private OrderService orderService;
//
//    @RequestMapping("/find/{id}")
//    public Order findOrder(@PathVariable("id") Long id ){
//        return demoService.findOrderById(id);
//    }
//
//    @RequestMapping("/initdata")
//    public String initData(){
//        return demoService.initData();
//    }
//
    @PostMapping("/order/create")
    public Order createOrder(@RequestBody Order order){
        return orderService.createOrder(order);
    }

    @RequestMapping("/order/get/{orderId}")
    public Order getOrder(@PathVariable("orderId") String orderId){
        return orderService.getOrder(orderId);
    }

    @RequestMapping("/order/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") String orderId){
        return orderService.deleteOrder(orderId);
    }
    @PostMapping("/order/list")
    public List<Order> listOrder(@RequestBody Order order){
        return orderService.listOrder(order);
    }

    @PostMapping("/orderitem/list")
    public List<OrderItem> listOrderItem(){
        return orderService.listOrderItem();
    }

    @PostMapping("/goods/create")
    public Goods createGoods(@RequestBody Goods goods){
        return orderService.createGoods(goods);
    }

    @RequestMapping("/goods/get/{goodsId}")
    public Goods getGoods(@PathVariable("goodsId") String goodsId){
        return orderService.getGoods(goodsId);
    }

    @RequestMapping("/goods/delete/{goodsId}")
    public String deleteGoods(@PathVariable("goodsId") String goodsId){
        return orderService.deleteGoods(goodsId);
    }

    @PostMapping("/goods/list")
    public List<Goods> listGoods(@RequestBody Goods goods){
        return orderService.listGoods(goods);
    }


    @PostMapping("/goodstype/create")
    public GoodsType createGoodsType(@RequestBody GoodsType goodsType){
        return orderService.createGoodsType(goodsType);
    }

    @RequestMapping("/goodstype/get/{goodsTypeId}")
    public GoodsType getGoodsType(@PathVariable("goodsTypeId") String goodsTypeId){
        return orderService.getGoodsType(goodsTypeId);
    }

    @RequestMapping("/goodstype/delete/{goodsTypeId}")
    public String deleteGoodsType(@PathVariable("goodsTypeId") String goodsTypeId){
        return orderService.deleteGoodsType(goodsTypeId);
    }

    @PostMapping("/goodstype/list")
    public List<GoodsType> listGoodsType(@RequestBody GoodsType goodsType){
        return orderService.listGoodsType(goodsType);
    }

//
//    @PostMapping("/goods/add/{user}")
//    public String addGoods( @PathVariable("user")String  user, Goods goods){
//        return demoService.createGoods(user,goods);
//    }

}
