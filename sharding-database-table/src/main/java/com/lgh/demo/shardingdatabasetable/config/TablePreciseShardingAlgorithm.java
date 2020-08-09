package com.lgh.demo.shardingdatabasetable.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class TablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        for (String tableName : availableTargetNames) {
            final String columnValue = shardingValue.getValue().toString();
            final String substring = columnValue.substring(28);
            final Integer integer = Integer.valueOf(substring);
            if (tableName.endsWith(integer % 2 + "")) {
                return tableName;
            }
        }
        throw new IllegalArgumentException();
    }

}
