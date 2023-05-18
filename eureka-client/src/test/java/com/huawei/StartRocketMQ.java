package com.huawei;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class StartRocketMQ {

    @Test
    public void StartMQ() throws IOException {
        String MQbatPath = "D:\\JavaNetWork\\RocketMQ\\startRocketMQ.bat";
        MQbatPath = MQbatPath.replace("\\","//" );
        Runtime.getRuntime().exec(MQbatPath);
    }
}
