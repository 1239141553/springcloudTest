package com.huawei.pojo;

import com.huawei.config.Doper;

public class OrderNew extends Order implements Doper {

    @Override
    public void doperTest() {
        this.doper();
    }

    public static void main(String[] args) {
        OrderNew orderNew = new OrderNew();
        orderNew.doperTest();
    }
}
