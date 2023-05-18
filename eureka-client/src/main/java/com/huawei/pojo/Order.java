package com.huawei.pojo;

import lombok.Data;

@Data
public class Order {


    private int id;
    private String type;
    private String name;
    private int orderNo;

    public void doper(){
        System.out.println("666");
    }
}
