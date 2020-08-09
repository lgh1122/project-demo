package com.laiyuezs.account.controller;

import com.laiyuezs.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Description：
 *
 */
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/debit",method = RequestMethod.GET)
    public Boolean debit(String userId, BigDecimal money) {
        accountService.debit(userId, money);

        return true;
    }
}
