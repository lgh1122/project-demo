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
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "t_order_item")
@Data
@EqualsAndHashCode
public final class OrderItem implements Serializable {
    

    private static final long serialVersionUID = 263434701950670170L;


    @Id
    @Column(length = 32)
    private String id;
    /**
     * 订单id
     */
    @Column
    private String orderId;

    @Column
    private String goodsId;
    @Column
    private String goodsName;
    /**
     * 商品数量
     */
    @Column
    private Integer goodsNum;


    /**
     * 总价格
     */
    private BigDecimal money;

    /**
     * 单价
     */
    private BigDecimal price;



}
