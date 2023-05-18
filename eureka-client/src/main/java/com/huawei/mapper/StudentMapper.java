package com.huawei.mapper;

import com.huawei.config.testSql.SqlLogs;
import com.huawei.pojo.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 凌凯轩
 * @since 2021-11-09
 */
public interface StudentMapper extends BaseMapper<Student> {

    @SqlLogs(taget = true)
    List<Student> getLogStudentList();
}
