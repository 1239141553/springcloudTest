package com.huawei.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.pojo.Student;
import com.huawei.pojo.base.BaseQuery;

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

    Page<Student> getStudentPage(BaseQuery baseQuery);

    List<Student> getLogStudentList();

    void Test();
}
