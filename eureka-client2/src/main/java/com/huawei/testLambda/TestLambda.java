package com.huawei.testLambda;



import com.huawei.pojo.User;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * lambda表达式的常用测试案例
 *
 * @author lingkaixuan
 */
public class TestLambda {

    public static void main(String[] args) {
        List<User> list =  getUserList();
        List<User> tList =  getUserList();

        Integer pageNo = 1;
        Integer pageSize = 10;
        Optional<User> any = tList.stream().findAny();
        System.out.println("any"+any);
        //分页
        list.stream().skip((long) (pageNo - 1) *pageSize).limit(pageSize).collect(Collectors.toList());
        //过滤
//        List<User> ageList = list.stream().filter(k -> k.getAge() > 20).collect(Collectors.toList());

        Map<String, List<User>> stringListMap = list.stream().collect(Collectors.groupingBy(k -> k.getName().split("_")[0]));
        //bigdecimal求和
        BigDecimal reduce = list.stream().map(User::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(reduce);
        //字符串拼接
        String s= list.stream().map(User::getName).collect(Collectors.joining("、"));
        System.out.println(s);

        //取出list的某个字段
        List<String> collect = list.stream().map(User::getName).collect(Collectors.toList());
        System.out.println(collect);

        //list集合依据某个字段进行正序
        List<User> collect1 = list.stream().sorted(Comparator.comparing(User::getAge)).collect(Collectors.toList());
        System.out.println(collect1);
        //list倒序
        List<User> collect2 = list.stream().sorted(Comparator.comparing(User::getAge).reversed()).collect(Collectors.toList());
        System.out.println(collect2);
        System.out.println("");
        //list分组成map
        Map<String, List<User>> groupMap = list.stream().collect(Collectors.groupingBy(x -> x.getName() + ":" + x.getAge()));
        groupMap.forEach((k, v) -> {
            //过滤
            List<User> collect3 = v.stream().filter(x -> x.getAge() > 11)
                    .collect(Collectors.toList());
            System.out.println(collect3);
        });
        String a = "a,b,c";
        System.out.println(a.substring(a.length()-1));
        //获取list某个字段的最大值
        int max = list.stream().mapToInt(User::getAge).summaryStatistics().getMax();
        System.out.println("\n" + max);

        //获取list某个字段的最小值
        int min = list.stream().mapToInt(User::getMoney).summaryStatistics().getMin();
        System.out.println("\n" + min);
        //获取Bigdecimal最小值
        BigDecimal maxPrice =new BigDecimal("999");
        BigDecimal minPrice = list.stream().map(User::getPrice).reduce(maxPrice, BigDecimal::min);
        System.out.println("bigdecimal:"+minPrice);
        //获取list某个字段的的合
        long sum = list.stream().mapToInt(User::getAge).summaryStatistics().getSum();
        System.out.println("\n" + sum);

        //某个字段的分组统计
        Map<String, Integer> map = list.stream().collect(Collectors.groupingBy(k->k.getId()+"_"+k.getName(), Collectors.summingInt(User::getMoney)));
        Map<String, BigDecimal> map1= list.stream().collect(Collectors.groupingBy(k->k.getId()+"_"+k.getName(), CollectorsUtil.summingBigDecimal((User::getPrice))));
        System.out.println("map1:"+map1);
        map.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });

//        //去重
//        List<String> myList = tList.stream().distinct().collect(Collectors.toList());
//        System.out.println("tList : "+myList);
        //依据某字段去重
        list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User::getName))), ArrayList::new));


        System.out.println("UserList : "+list);

        String name = "lkx";
        //删除list中满足条件的对象
        list.removeIf(k -> k.getName().equals(name));
        System.out.println(list);

    }

    /**
     * 获取User
     * @return
     */
    private static List<User> getUserList(){
        List<String> tList = new ArrayList<>();
        tList.add("1");
        tList.add("1");
        tList.add("2");
        tList.add("2");
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setAge(12);
        user.setName("lkx");
        user.setMoney(15);
        user.setPrice(new BigDecimal("111.00"));
        list.add(user);

        User user1 = new User();
        user1.setAge(13);
        user1.setName("lkx1");
        user1.setMoney(15);
        user1.setPrice(new BigDecimal("222.00"));
        list.add(user1);

        User user2 = new User();
        user2.setAge(11);
        user2.setName("lkx2");
        user2.setMoney(15);
        user2.setPrice(new BigDecimal("333.00"));
        list.add(user2);

        User user3 = new User();
        user3.setAge(11);
        user3.setName("lkx_2");
        user3.setMoney(16);
        user3.setPrice(new BigDecimal("444.00"));
        list.add(user3);
        return list;
    }

}
