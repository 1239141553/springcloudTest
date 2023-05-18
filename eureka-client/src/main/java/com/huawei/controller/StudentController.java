package com.huawei.controller;


import com.huawei.config.Generate.GenerateResult;
import com.huawei.config.testField.Apple;
import com.huawei.config.testField.FruitInfoUtil;
import com.huawei.config.testField.TransferFieldMethod;
import com.huawei.config.testField.Utils;
import com.huawei.pojo.Student;
import com.huawei.service.StudentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 凌凯轩
 * @since 2021-11-09
 */
@RestController
@RequestMapping("/student")
@CrossOrigin
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/no_session/test")
//    @TestMethod(projectName = "1122")
    public String test() {
        List<Student> student = new ArrayList<>();
        Student student1 = new Student();
        student1.setName("111");
        student.add(student1);
        System.out.println(student);
        System.out.println("访问成功");
        return "访问成功";
    }

    @RequestMapping("testAop")
    @TransferFieldMethod(target = "123")
    public String testAop() {
        List<Student> studentList = studentService.getLogStudentList();
        List<Student> matchings = Utils.matchings(studentList);
        System.out.println(matchings);
        FruitInfoUtil.getFruitInfo(Apple.class);
        return "访问成功";
    }

    @GetMapping("/no_session/testOne")
    public GenerateResult<String> testOne() {
      return GenerateResult.ok("666");
    }

    @GetMapping("/test")
    public GenerateResult<String> test1() {
        studentService.Test();
        return GenerateResult.ok("666");
    }

    @GetMapping("/sendRabbitMq")
    public GenerateResult<String> sendRabbitMq() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "hello word";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //将消息携带绑定键值：testRouting 发送到交换机directExchange
        rabbitTemplate.convertAndSend("directExchange", "testRouting", map);
        return GenerateResult.ok("123");
    }
}

