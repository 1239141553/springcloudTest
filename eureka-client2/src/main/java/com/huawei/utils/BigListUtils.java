package com.huawei.utils;

import org.apache.commons.collections4.ListUtils;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 超长数组处理，针对可能长度很大的list进行分批次处理
 * <p/>
 *
 * @author lkx
 * @date 2020/1/16
 */
public class BigListUtils {

    private BigListUtils() {}

    /**
     * 默认一次处理的元素个数
     */
    private static final int DEFAULT_ONCE_SIZE = 50;

    /**
     * 分批处理列表
     *
     * @param targetList
     * @param exe
     * @param onceSize
     */
    public static <T> void batchHandleList(List<T> targetList, PartitionExecuter<T> exe, int onceSize) {
        if (targetList == null) {
            return;
        }
        if (targetList.size() <= onceSize) {
            exe.execute(targetList);
        } else {
            List<List<T>> parentList = ListUtils.partition(targetList, onceSize);
            for (List<T> subList : parentList) {
                exe.execute(subList);
            }
        }
    }

    /**
     * 分批处理列表
     *
     * @param targetList
     * @param exe
     */
    public static <T> void batchHandleList(List<T> targetList, PartitionExecuter<T> exe) {
        batchHandleList(targetList, exe, DEFAULT_ONCE_SIZE);
    }

    /**
     * 分段处理函数
     */
    @FunctionalInterface
    public interface PartitionExecuter<T> {

        /**
         * 执行业务
         *
         * @param list
         */
        void execute(List<T> list);
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2", "3", "4", "5");
        BigListUtils.batchHandleList(list, subList -> print(subList), 3);
    }
    private static void print(List<String> list){
        System.out.println(list);
    }
}
