package com.laiyuezs.account.dao;

import com.laiyuezs.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author liliang
 * @Date 2020/2/24 23:23
 * @Description
 **/
public interface AccountRepository extends JpaRepository<Account,Long> {

    Account findByUserId(String userId);
}
