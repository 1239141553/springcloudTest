package com.huawei.listener;

import com.alibaba.fastjson.JSONObject;
import com.huawei.pojo.Order;
import com.huawei.service.OrderService;
import com.huawei.service.TransactionLogService;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderTransactionListener implements TransactionListener {

    @Autowired
    OrderService orderService;

    @Autowired
    TransactionLogService transactionLogService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        logger.info("开始执行本地事务....");
        LocalTransactionState state;
        try{
            String body = new String(message.getBody());
            Order order = JSONObject.parseObject(body, Order.class);
            orderService.createOrder(order,message.getTransactionId());
            state = LocalTransactionState.COMMIT_MESSAGE;
            logger.info("本地事务已提交。{}",message.getTransactionId());
        }catch (Exception e){
            logger.info("执行本地事务失败。{}",e);
            state = LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return state;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        logger.info("开始回查本地事务状态。{}",messageExt.getTransactionId());
        LocalTransactionState state;
        String transactionId = messageExt.getTransactionId();
        if (transactionLogService.get(transactionId)>0){
            state = LocalTransactionState.COMMIT_MESSAGE;
        }else {
            state = LocalTransactionState.ROLLBACK_MESSAGE;
        }
        logger.info("结束本地事务状态查询：{}",state);
        return state;
    }
}
