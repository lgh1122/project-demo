package com.lgh.demo.shardingdatabasetable.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品表
 */
@Entity
@Table(name = "t_goods")
@Data
public class Goods implements Serializable {


    private static final long serialVersionUID = 26343470197467789L;

    @Id
    @Column(length = 32)
    private String id;
    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 描述
     */
    @Lob
    private String remark;

    /**
     * 状态
     */
    private String status;

    /**
     * 商品类型id
     */
    private String typeId;


}
