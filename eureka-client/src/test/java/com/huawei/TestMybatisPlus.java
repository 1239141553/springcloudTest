package com.huawei;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.generator.config.querys.AbstractDbQuery;
import com.huawei.mapper.TbOrderMapper;
import com.huawei.pojo.TbOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMybatisPlus {

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Test
    public void testSelect(){
        QueryWrapper<TbOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        wrapper.eq("receiver", "李嘉诚");
        List<TbOrder> list= tbOrderMapper.selectList(wrapper);
        System.out.println(list);
    }

}
