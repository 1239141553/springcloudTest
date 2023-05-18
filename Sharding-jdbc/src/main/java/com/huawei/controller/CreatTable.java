package com.huawei.controller;

import com.huawei.pojo.Student;
import com.huawei.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CreatTable {

    @Autowired
    private StudentService studentService;

    @GetMapping("/createTable")
    public List<Student> createTable(){
        List<Student> studentList = studentService.getStudentList();
        System.out.println(studentList);
        return studentList ;
    }

    @GetMapping("/createTable1")
    public String createTable1(){
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student = new Student();
            student.setName("lkx"+i);
            student.setAge(i);
            student.setSex(1);
            list.add(student);
        }
        List<Student> studentList = studentService.save(list);
        System.out.println(studentList);
        return "访问成功";
    }
}
