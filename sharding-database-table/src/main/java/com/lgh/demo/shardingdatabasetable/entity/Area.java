package com.lgh.demo.shardingdatabasetable.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * describe:省市县关联表，树形结构，用来选择或者展示区域用
 *
 * @Author Wangs
 * @date 2019/02/20
 */
@Data
@Entity
@Table(name = "t_area")
public class Area implements Serializable {
    private static final long serialVersionUID = -4711308720935775778L;


    @Id
    @Column(nullable = false, length = 32)
    private String id;

    @Column(nullable = false, length = 32)
    private String name;

    @Column(length = 12)
    private Integer seqNum;

    /**
     * 父级
     */
    @Column(length = 32)
    private String parentId;


}
