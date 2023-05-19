package com.huawei.common;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;

@Component
public class QueueConfig {

    @Bean
    public ArrayBlockingQueue<String> getQueue(){
        return new ArrayBlockingQueue<String>(1);
    }
}
