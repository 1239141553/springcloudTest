package com.huawei.controller;


import cn.hutool.core.lang.Assert;
import com.huawei.Producer.CommonProducer;
import com.huawei.common.GeneratorCodeConfigHttp;
import com.huawei.utils.RedisUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/test")
public class CreatTable {

    @Autowired
    private GeneratorCodeConfigHttp generatorCodeConfigHttp;
    @Autowired
    private CommonProducer commonProducer;

    @Autowired
    private ArrayBlockingQueue arrayBlockingQueue;
    @Resource
    private RedissonClient redissonClient;

    private static Lock lock = new ReentrantLock();

    @GetMapping("/createTable")
    public String createTable(@RequestParam("tableName") String tableName) {
        generatorCodeConfigHttp.create(tableName);
        return "访问成功";
    }

    @GetMapping("/setRedis")
    public String setRedis(@RequestParam("key") String key) {
        RedisUtils.set(key, "12322");
        return "访问成功";
    }

    @GetMapping("/setMsg")
    public String setMsg(@RequestParam("key") String key) {
        try {
            commonProducer.sendMsg("order1", "tag", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "访问成功";
    }

    @GetMapping("/setMsg1")
    public String setMsg1(@RequestParam("key") String key) {
        try {
            commonProducer.sendMsg("order", "tag", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "访问成功";
    }

    @GetMapping("/testQueue")
    public String testQueue(@RequestParam("key") String key) {
        try {
            arrayBlockingQueue.put(key);
            System.out.println(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "访问成功";
    }

    @GetMapping("/takeQueue")
    public String takeQueue(@RequestParam("key") String key) {
        try {
            Object take = arrayBlockingQueue.take();
            System.out.println(take);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "访问成功";
    }

    @GetMapping("/takeLock")
    public String takeLock(@RequestParam("key") String key) throws InterruptedException {
        lock.lock();
        try {
            System.out.println(key);
            Thread.sleep(10000);
            System.out.println(key + "is");
            return "666";
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("释放锁");
            lock.unlock();
        }
        return "666";
    }

    @GetMapping("/takeRedisson")
    public String takeRedisson(@RequestParam("key") String key) {
        RLock lock = redissonClient.getLock("redisKey");
        try {
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                //业务处理
                System.out.println(key);
                Thread.sleep(10000);
                System.out.println(key + "is");
            } else {
                Assert.isTrue(false, "排队中,请稍后重试!");
            }
        } catch (InterruptedException e) {
            Assert.isTrue(false, "请勿重复操作!");
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
        return "666";
    }

}
