package com.laiyuezs.business.controller;

import com.laiyuezs.business.service.BusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author liliang
 * @Date 2020/2/24 21:18
 * @Description
 **/

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;
    private final static Logger logger = LoggerFactory.getLogger(BusinessController.class);

    /**
     * 购买下单，模拟全局事务提交
     * @return
     */
    @RequestMapping(value = "/purchase/commit",method = RequestMethod.GET)
    public Boolean purchaseCommit(){
        businessService.purchase("1001", "2001", 1);
        return true;
    }





    /**
     * 购买下单，模拟全局事务回滚
     *
     * @return
     */
    @RequestMapping(value = "/purchase/rollback",method = RequestMethod.GET)
    public Boolean purchaseRollback() {
        try {
            businessService.purchase("1003", "2001", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    /**
     * 购买下单，无全局事务全局事务提交
     * @return
     */
    @RequestMapping(value = "/purchase/notransaction/commit",method = RequestMethod.GET)
    public Boolean purchaseCommitNoTransaction(){
        businessService.purchaseNoTransaction("1001", "2001", 1);
        return true;
    }
    /**
     * 购买下单，模拟全局事务回滚
     *
     * @return
     */
    @RequestMapping(value = "/purchase/notransaction/rollback",method = RequestMethod.GET)
    public Boolean purchaseRollbackNoTransaction() {
        try {
            businessService.purchaseNoTransaction("1003", "2001", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }



}
