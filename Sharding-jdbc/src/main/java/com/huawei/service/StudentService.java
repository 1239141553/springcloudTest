package com.huawei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.pojo.Student;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 凌凯轩
 * @since 2021-11-09
 */
public interface StudentService extends IService<Student> {
    /**
     * 查询学生
     * @return
     */
    List<Student> getStudentList();

    /**
     * 保存
     * @param studentList
     * @return
     */
    List<Student> save(List<Student> studentList);
}
