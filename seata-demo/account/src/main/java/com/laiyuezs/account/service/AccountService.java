package com.laiyuezs.account.service;

import com.laiyuezs.account.dao.AccountRepository;
import com.laiyuezs.account.entity.Account;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Description：
 *
 * @author fangliangsheng
 * @date 2019-04-05
 */
@Service
public class AccountService {
    private final static Logger logger = LoggerFactory.getLogger(AccountService.class);


    @Autowired
    private AccountRepository accountRepository;

//    @Transactional(rollbackFor = Exception.class)
    @Transactional
    public void debit(String userId, BigDecimal num) {
        logger.debug("全局事务id ：" + RootContext.getXID());

        Account account = accountRepository.findByUserId(userId);

        System.out.println("---------account-----------"+account);
        account.setMoney(account.getMoney().subtract(num));
        accountRepository.save(account);


    }
}
