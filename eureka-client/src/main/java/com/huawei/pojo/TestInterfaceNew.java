package com.huawei.pojo;

public class TestInterfaceNew implements TestInterface{
    @Override
    public void test() {
        TestInterface.test1();
    }

    public static void main(String[] args) {
        TestInterfaceNew testInterfaceNew = new TestInterfaceNew();
        testInterfaceNew.test();
    }
}
