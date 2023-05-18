package com.huawei.Producer;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lkx
 * 普通消费者
 */
@Component
public class CommonProducer {
    @Resource
    private DefaultMQProducer producer;

//    @Value("${rocketmq.producer.group}")
//    private String productGroup;
//    @Value("${rocketmq.name-server}")
//    private String nameServer;
//    @Value("${rocketmq.producer.timeOut}")
//    private String timeOut;
    //    //执行任务的线程池
//    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60,
//            TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));
    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 发送消息
     *
     * @param topic
     * @param tag
     * @param t
     * @param <T>
     * @throws MQClientException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQBrokerException
     */
    public <T> void sendMsg(String topic, String tag, T t) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        // 4.构建消息对象，主要是设置消息的主题 标签 内容
        Message message = new Message(topic, tag, JSONObject.toJSONString(t).getBytes());
        // 5.发送消息，超时时间
        SendResult result = producer.send(message, 10000);
        log.info("发送消息成功,topic:{},tag:{},msg:{}", topic, tag, JSONObject.toJSON(t));
    }


}
