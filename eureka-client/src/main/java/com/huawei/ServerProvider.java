package com.huawei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@MapperScan("com.huawei.mapper")
@SpringBootApplication
public class ServerProvider {
    public static void main(String[] args) {
        SpringApplication.run(ServerProvider.class,args);
    }
}
