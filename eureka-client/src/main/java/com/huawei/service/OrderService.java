package com.huawei.service;

import com.huawei.pojo.Order;
import org.apache.rocketmq.client.exception.MQClientException;

public interface OrderService {
    void createOrder(Order order, String transactionId);
    void sendOreadMessage(Order order)throws MQClientException;
}
