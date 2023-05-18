package com.huawei.controller;


import com.huawei.Producer.CommonProducer;
import com.huawei.common.GeneratorCodeConfigHttp;
import com.huawei.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreatTable {

    @Autowired
    private GeneratorCodeConfigHttp generatorCodeConfigHttp;
    @Autowired
    private CommonProducer commonProducer;
    @GetMapping("/createTable")
    public String createTable(@RequestParam("tableName") String tableName){
        generatorCodeConfigHttp.create(tableName);
        return "访问成功";
    }

    @GetMapping("/setRedis")
    public String setRedis(@RequestParam("key") String key){
        RedisUtils.set(key,"12322");
        return "访问成功";
    }

    @GetMapping("/setMsg")
    public String setMsg(@RequestParam("key") String key){
        try {
            commonProducer.sendMsg("order1","tag",key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "访问成功";
    }

    @GetMapping("/setMsg1")
    public String setMsg1(@RequestParam("key") String key){
        try {
            commonProducer.sendMsg("order","tag",key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "访问成功";
    }
}
