package com.huawei.controller;


import com.huawei.pojo.Order;
import com.huawei.pojo.Student;
import com.huawei.service.OrderService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("test")
    public String test(){
        Student student = new Student();
        return "访问成功";
    }
    @RequestMapping("testMQ")
    public String testMQ(){
        try {
            Order order = new Order();
            order.setId(1);
            order.setName("衬衣");
            order.setOrderNo(1101);
            order.setType("衣服");
            orderService.sendOreadMessage(order);
        } catch (MQClientException e) {
            return "消磁发送失败请检查MQ服务器";
        }
        return "消息发送成功";
    }
}
