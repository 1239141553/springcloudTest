package com.huawei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huawei.mapper.StudentMapper;
import com.huawei.pojo.Student;
import com.huawei.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    @Resource
    private StudentMapper studentMapper;
    @Override
    public List<Student> getStudentList() {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", "lkx1");
        List<Student> students = studentMapper.selectList(queryWrapper);
        return students;
    }

    @Override
    public List<Student> save(List<Student> studentList) {
        studentList.forEach(k->{
            studentMapper.insert(k);
        });
        return studentList;
    }
}
