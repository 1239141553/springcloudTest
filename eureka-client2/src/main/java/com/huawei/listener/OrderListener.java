//package com.huawei.listener;
//
//import com.alibaba.fastjson.JSONObject;
//import com.huawei.pojo.Order;
//import com.huawei.service.PointsService;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class OrderListener implements MessageListenerConcurrently {
//
//    @Autowired
//    PointsService pointsService;
//    Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Override
//    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
//        logger.info("消费者线程监听到消息。");
//        try{
//            for (MessageExt message:list) {
//                logger.info("开始处理订单数据，准备增加积分....");
//                Order order  = JSONObject.parseObject(message.getBody(), Order.class);
//                pointsService.increasePoints(order);
//            }
//            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//        }catch (Exception e){
//            logger.error("处理消费者数据发生异常。{}",e);
//            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//        }
//    }
//}
