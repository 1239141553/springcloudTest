package com.huawei.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class EsClient {

    private static final Logger logger = LoggerFactory.getLogger(EsClient.class);

    private static TransportClient transportClient;

    public static TransportClient getTransportClient() {
        return transportClient;
    }

    @Value("${elasticSearchPort}")
    private String elasticSearchPort;
    @Value("${elasticSearchClusterName}")
    private String elasticSearchClusterName;
    @Value("${elasticSearchPoolSize}")
    private String elasticSearchPoolSize;

    @Value("${elasticSearchIps}")
    public void connectionInit(String elasticSearchIps) {
        try {
            //设置netty内存管理非池化
            String currentValue = System.getProperty("io.netty.allocator.type");
            if (currentValue == null) {
                System.setProperty("io.netty.allocator.type","unpooled");
            }
            // 配置信息
            Settings esSetting = Settings.builder().put("cluster.name", elasticSearchClusterName).put("thread_pool" +
                    ".search.size", Integer.parseInt(elasticSearchPoolSize))//增加线程池个数，暂时设为5
                    .build();
            //配置信息Settings自定义,下面设置为EMPTY
            transportClient = new PreBuiltTransportClient(esSetting);
            String[] elasticSearchIpArray = elasticSearchIps.split(",");
            for (String elasticSearchIp : elasticSearchIpArray) {
                TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(elasticSearchIp),
                        Integer.valueOf(elasticSearchPort));
                transportClient.addTransportAddresses(transportAddress);
            }
        }
        catch (Exception e) {
            logger.error("elasticsearch TransportClient create error!!!", e);
        }
    }
}