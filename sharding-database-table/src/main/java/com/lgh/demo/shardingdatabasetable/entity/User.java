package com.lgh.demo.shardingdatabasetable.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户信息表
 * @author 17519
 */
@Entity
@Data
@Table(name = "t_user")
public class User implements Serializable {
    private static final long serialVersionUID = 7230052310725727465L;
    @Id
    @Column(length = 32)
    private String id;
    @Column
    private String phone;
    @Column(length = 64)
    private String name;
    @Column(length = 32)
    private String email;

    private String password;
    /**
     * 自我介绍
     */
    @Column(length = 128)
    private String remark;


}
