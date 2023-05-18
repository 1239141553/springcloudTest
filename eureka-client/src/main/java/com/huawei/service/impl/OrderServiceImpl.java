package com.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.huawei.Producer.TransactionProducer;
import com.huawei.pojo.Order;
import com.huawei.service.OrderService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    TransactionProducer producer;

   // Snowflake snowflake = new Snowflake(1,1);
    Logger logger = LoggerFactory.getLogger(this.getClass());

    //执行本地事务时调用，将订单数据和事务日志写入本地数据库
    //@Transactional
    @Override
    public void createOrder(Order orderD,String transactionId){

        //1.创建订单
        logger.info("创建订单信息");
        //2.写入事务日志
        logger.info("将事务记录到事务表");

    }

    //前端调用，只用于向RocketMQ发送事务消息
    @Override
    public void sendOreadMessage(Order order) throws MQClientException {
        //order.setId(snowflake.nextId());
        //order.setOrderNo(snowflake.nextIdStr());
        producer.send(JSON.toJSONString(order),"order");
    }

}

