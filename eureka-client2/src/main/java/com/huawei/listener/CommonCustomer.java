package com.huawei.listener;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author lkx
 * 消费者公共
 */
@Configuration
public class CommonCustomer {

    private DefaultMQPushConsumer consumer;

    @Value("${rocketmq.consumer.group}")
    private String consumerGroup;
    @Value("${rocketmq.name-server}")
    private String nameServer;
    @Value("${rocketmq.consumer.timeOut}")
    private String timeOut;

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumeTimeout(Integer.parseInt(timeOut));
        consumer.subscribe("order","tag");
        // 4.设置一个回调函数，并在函数中编写接收到的消息之后的处理办法
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                // 消费逻辑
                System.out.println("message=====> " + list);
                // 返回消费成功状态
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }


}
