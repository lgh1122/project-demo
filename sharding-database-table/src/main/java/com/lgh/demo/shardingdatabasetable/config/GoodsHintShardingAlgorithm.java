package com.lgh.demo.shardingdatabasetable.config;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.ArrayList;
import java.util.Collection;

public class GoodsHintShardingAlgorithm implements HintShardingAlgorithm<String>  {


    @Override
    public Collection<String> doSharding(Collection<String> collection, HintShardingValue<String> hintShardingValue) {
        Collection<String> result = new ArrayList<>();
        for (String each : collection) {
            for (String value : hintShardingValue.getValues()) {
                ((ArrayList<String>) result).add(value);
                break;
            }
        }
        return result;
    }
}
