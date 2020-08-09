package com.laiyuezs.storage.dao;

import com.laiyuezs.storage.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author liliang
 * @Date 2020/2/24 23:09
 * @Description
 **/
public interface StorageRepository extends JpaRepository<Storage, Long> {

    Storage findByCommodityCode(String commodityCode);
}
