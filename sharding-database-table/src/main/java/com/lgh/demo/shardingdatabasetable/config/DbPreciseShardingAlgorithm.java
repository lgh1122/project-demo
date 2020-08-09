package com.lgh.demo.shardingdatabasetable.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class DbPreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        for (String dbName : availableTargetNames) {
            final String columnValue = shardingValue.getValue().toString();
            final String substring = columnValue.substring(28);
            final Integer integer = Integer.valueOf(substring);
            int s = integer % 4 ;
            int suffix = 0;
            suffix = s <2 ? 0 : 1;
            if (dbName.endsWith(suffix + "")) {
                return dbName;
            }
        }
        throw new IllegalArgumentException();
    }

}
