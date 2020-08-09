package com.laiyuezs.storage.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "storage_tbl")
public class Storage {

    @Id
    private Long id;
    private String commodityCode;
    private Integer count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", commodityCode='" + commodityCode + '\'' +
                ", count=" + count +
                '}';
    }
}
