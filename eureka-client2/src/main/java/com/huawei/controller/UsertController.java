package com.huawei.controller;


import com.huawei.config.GenerateResult;
import com.huawei.feign.TestFeign;
import com.huawei.pojo.UserRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/student")
public class UsertController {

    @Resource
    private TestFeign testFeign;

    @GetMapping ("/testThis")
//    @HystrixCommand(fallbackMethod = "testFeign")
    public String testThis(){
        log.info("准备去访问Feign客户端");
        return testFeign.testOne();
    }

    public String testFeign(String name){

        return "123";
    }

    @PostMapping("/testValid")
    public String testValid(@Valid @RequestBody UserRsp userRsp){
        log.info("调用feign");
        return "调用成功";
    }

    @GetMapping("/test")
    public GenerateResult<String> test(){
        log.info("调用成功");
        return GenerateResult.ok("调用成功");
    }
}
