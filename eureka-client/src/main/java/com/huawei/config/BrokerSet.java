package com.huawei.config;

import org.apache.rocketmq.common.BrokerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BrokerSet{

    @Bean
    public BrokerConfig brokerConfig(){
        BrokerConfig brokerConfig = new BrokerConfig();
        brokerConfig.setTransactionCheckInterval(10000); //回查频率10秒一次
        brokerConfig.setTransactionCheckMax(3);  //最大检测次数为3
        return brokerConfig;
    }
}
