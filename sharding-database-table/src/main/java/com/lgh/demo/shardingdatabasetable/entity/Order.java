/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lgh.demo.shardingdatabasetable.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单表
 * @author 17519
 */
@Entity
@Table(name = "t_order")
@Data
public final class Order implements Serializable {

    private static final long serialVersionUID = 661434701950670670L;

    /**
     * 订单id
     */
    @Id
    @Column(length = 32,name = "order_id")
    private String id;

    /**
     * 买家id
     */
    @Column
    private String userId;
    /**
     *  收货地址
     */
    @Column
    private String  receiveAddress ;

    /**
     * 总价格
     */
    private BigDecimal  totalMoney ;

    /**
     * 创建时间
     */
    @Column
    private Long createTime;

    /**
     * 完成时间
     */
    @Column
    private Long finishTime;
    /**
     * 状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 订单明细
     */
    @OneToMany(cascade={CascadeType.REMOVE})
    @JoinColumn(name = "orderId")
    //@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
    @ToString.Exclude()
    private List<OrderItem> orderItems;

    /**
     * 备注
     */
//    @Lob
    @Column(columnDefinition="LONGTEXT")
    private String remark;

}
