package com.huawei.config.testField;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class Utils {

    public static <T> List<T> matchings(List<T> datas) {
        if (CollUtil.isEmpty(datas)) {
            return datas;
        }
        System.out.println("进入标签");
        datas.forEach(k->{
            Class<?> target = k.getClass();
            Field[] fields = ReflectUtil.getFields(target);
            for (Field field : fields) {
                if (field.isAnnotationPresent(TransferField.class)) {
                    TransferField transferField = field.getAnnotation(TransferField.class);
                    if (Objects.nonNull(transferField)) {
                        String name = field.getName();
                        Class<?> type = field.getType();
                        PropertyDescriptor pd = null;
                        try {
                            pd = new PropertyDescriptor(name, target);
                            Method getMethod = pd.getReadMethod();//获取读的方法
                            field.setAccessible(true);
                            Object invoke = getMethod.invoke(k);
                            if(type.equals(String.class)){
                                Method writeMethod = pd.getWriteMethod();
                                writeMethod.invoke(k,"123");
                            }
                        } catch (IntrospectionException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return datas;
    }
}
