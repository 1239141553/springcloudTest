package com.huawei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huawei.mapper.StudentMapper;
import com.huawei.pojo.Student;
import com.huawei.pojo.base.BaseQuery;
import com.huawei.service.StudentService;
import com.huawei.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 凌凯轩
 * @since 2021-11-09
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> getStudentList() {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        List<Student> students = studentMapper.selectList(queryWrapper);
        return students;
    }

    @Override
    public Page<Student> getStudentPage(BaseQuery baseQuery) {
        Page<Student> studentPage = new Page<>(baseQuery.getPageNum(),baseQuery.getPageSize());
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        studentMapper.selectPage(studentPage,queryWrapper);
        return studentPage;
    }

    @Override
    public List<Student> getLogStudentList() {
        List<Student> students= studentMapper.getLogStudentList();
        return students;
    }

    /**
     * 测试
     */
    @Override
    public void Test(){
//        RedisUtils.set("123","name");
        RedisUtils.set("234","NAME",6);
    }
}
