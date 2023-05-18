package com.huawei.service.impl;

import com.huawei.pojo.Order;
import com.huawei.service.PointsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PointsServiceImpl implements PointsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void increasePoints(Order order) {
        System.out.println(order);
        logger.info("存入积分系统");
    }
}
