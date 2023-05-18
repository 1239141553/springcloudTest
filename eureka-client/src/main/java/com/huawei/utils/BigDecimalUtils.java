package com.huawei.utils;

import java.math.BigDecimal;

/**
 * bigdecimal工具类
 * @author lkx
 */
public class BigDecimalUtils {

    /**
     * 比较大于
     * @param compared
     * @param target
     * @return
     */
    public static boolean bigThanTarget(BigDecimal compared, BigDecimal target){
        if(compared==null||target==null){
            return false;
        }
        if(compared.compareTo(target) > 0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 比较大于等于
     * @param compared
     * @param target
     * @return
     */
    public static boolean bigAndEqualThanTarget(BigDecimal compared, BigDecimal target){
        if(compared==null||target==null){
            return false;
        }
        if(compared.compareTo(target) > -1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 比较等于
     * @param compared
     * @param target
     * @return
     */
    public static boolean EqualTarget(BigDecimal compared, BigDecimal target){
        if(compared==null||target==null){
            return false;
        }
        if(compared.compareTo(target) ==0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 比较小于
     * @param compared
     * @param target
     * @return
     */
    public static boolean lessThanTarget(BigDecimal compared, BigDecimal target){
        if(compared==null||target==null){
            return false;
        }
        if(compared.compareTo(target) < 0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 比较小于等于
     * @param compared
     * @param target
     * @return
     */
    public static boolean lessAndEqualThanTarget(BigDecimal compared, BigDecimal target){
        if(compared==null||target==null){
            return false;
        }
        if(compared.compareTo(target) < 1){
            return true;
        }else {
            return false;
        }
    }
}
