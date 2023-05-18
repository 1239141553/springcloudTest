package com.huawei.config.testField;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class TransferConfigurer {


    @PostConstruct
    public void init() {
        log.info("=======开启注解方法字典转义=======");
    }


    @Pointcut("@annotation(com.huawei.config.testField.TransferFieldMethod)")
    public void transferField() {

    }


    @Around(value = "transferField()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) {
        Object data = null;
        try {
            data = joinPoint.proceed();
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature)signature;
            Method targetMethod = methodSignature.getMethod();
            Class clazz = targetMethod.getClass();
            System.out.println(clazz.getDeclaredFields());
            return data;
        } catch (Throwable throwable) {
            log.info("进行数据字典转义异常");
        }
        return data;
    }


}