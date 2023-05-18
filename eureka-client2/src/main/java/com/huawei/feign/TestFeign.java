package com.huawei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "client-1",url = "http://127.0.0.1:8081",path = "/student")
public interface TestFeign {
    @GetMapping("/testOne")
    String testOne();
}
