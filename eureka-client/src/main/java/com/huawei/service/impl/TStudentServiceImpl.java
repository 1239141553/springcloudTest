package com.huawei.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.pojo.TStudent;
import com.huawei.mapper.TStudentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author astupidcoder
 * @since 2021-11-09
 */
@Service
public class TStudentServiceImpl extends ServiceImpl<TStudentMapper, TStudent> implements IService<TStudent> {

}
