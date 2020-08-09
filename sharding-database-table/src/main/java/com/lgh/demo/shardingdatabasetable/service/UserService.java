package com.lgh.demo.shardingdatabasetable.service;

import com.lgh.demo.shardingdatabasetable.entity.User;
import com.lgh.demo.shardingdatabasetable.repository.UserRepository;
import com.lgh.demo.shardingdatabasetable.util.IdGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 17519
 */
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    public String register(User userValue) {
        userValue.setId(IdGenerator.getNextId(User.class));
        userRepository.save(userValue);
        return "success";
    }
}
