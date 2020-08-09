package com.laiyuezs.storage.service;

import com.laiyuezs.storage.dao.StorageRepository;
import com.laiyuezs.storage.entity.Storage;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description：
 *
 */
@Service
public class StorageService {

    private final static Logger logger = LoggerFactory.getLogger(StorageService.class);

    @Autowired
    private StorageRepository storageRepository;

    @Transactional
    public void deduct(String commodityCode, int count) {
        logger.debug("全局事务id ：{}" , RootContext.getXID());

        Storage storage = storageRepository.findByCommodityCode(commodityCode);
        logger.debug("------商品id-----{}-------库存------{}",commodityCode,storage.getCount());
        storage.setCount(storage.getCount() - count);
        logger.debug("----Storage-----{}",storage);
        storageRepository.save(storage);
    }
}
