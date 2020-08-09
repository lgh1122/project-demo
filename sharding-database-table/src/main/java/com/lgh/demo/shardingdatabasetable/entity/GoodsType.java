package com.lgh.demo.shardingdatabasetable.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * describe:商品类型表
 *
 * @Author Wangs
 * @date 2019/02/20
 */
@Data
@Entity
@Table(name = "t_goods_type")
public class GoodsType implements Serializable {
    private static final long serialVersionUID = -4711308720935775778L;


    @Id
    @Column(nullable = false, length = 32)
    private String id;

    @Column(nullable = false, length = 32)
    private String name;

    @Column(length = 12)
    private Integer seqNum;


}
