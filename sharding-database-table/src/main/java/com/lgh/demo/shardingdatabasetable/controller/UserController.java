package com.lgh.demo.shardingdatabasetable.controller;

import com.lgh.demo.shardingdatabasetable.entity.User;
import com.lgh.demo.shardingdatabasetable.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 17519
 */
@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
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
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public String register(@RequestBody User userValue){
        return userService.register(userValue);
    }
//
//    @PostMapping("/goods/add/{user}")
//    public String addGoods( @PathVariable("user")String  user, Goods goods){
//        return demoService.createGoods(user,goods);
//    }

}
