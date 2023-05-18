package com.huawei.controller;

import com.huawei.pojo.DiscussPost;
import com.huawei.service.EsTestServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
public class controller {

   @Autowired
    private EsTestServer esTestServer;

    @GetMapping("/test")
    public String test(){
        System.out.println("test");
        return "test";
    }

    @GetMapping("/saveEs")
    public String saveEs(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setContent("112");
        discussPost.setCommentCount(123);
        discussPost.setCreateTime(new Date());
        discussPost.setStatus(1);
        discussPost.setTitle("EsTest");
        esTestServer.saveEs(discussPost);
        return "保存Es成功";
    }

    @GetMapping("/search")
    public String search(@RequestParam("content") String content){
        try {
            List<DiscussPost> search = esTestServer.search(content, 1, 10);
            System.out.println(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "查询成功";
    }
}
