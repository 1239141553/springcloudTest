//package com.huawei.listener;
//
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//
//@Configuration
//public class MQCustomer {
//
//    String consumerGroup = "consumer-group";
//
//
//    @Autowired
//    OrderListener orderListener;
//
//    @PostConstruct
//    public void init() throws MQClientException {
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
//        consumer.setNamesrvAddr("127.0.0.1:9876");
//        consumer.subscribe("order","tag");
//        consumer.registerMessageListener(orderListener);
//        consumer.start();
//    }
//}
