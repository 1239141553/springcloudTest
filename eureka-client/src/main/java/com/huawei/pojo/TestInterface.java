package com.huawei.pojo;

public interface TestInterface {
    void test();
    static void test1(){
        System.out.println("666");
    }
    default void test2(){
        System.out.println("777");
    }
}
