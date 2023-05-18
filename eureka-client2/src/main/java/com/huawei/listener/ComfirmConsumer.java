package com.huawei.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ComfirmConsumer {
    /**
     * 路由模式
     *
     * @param message 消息内容
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("directExchange"), // 交换机(直连)
            key = "testRouting",                // 路由key
            value = @Queue("testQueue"))) //监听队列的名称
    public void fanoutConsumer1(Message message, Channel channel) throws IOException {
        log.info("发布订阅模式消费者1接收到消息：{}", message);
        try{
            //注意进行迷瞪操作
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            // 手动进行消息确认
            System.out.println(1/0);
            channel.basicAck(deliveryTag,false);
        }catch (Exception e){
            //优化方案  存到数据库 使用xxljob进行定时任务推送
            log.info("存入数据库：{}", message);
//            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }
}
