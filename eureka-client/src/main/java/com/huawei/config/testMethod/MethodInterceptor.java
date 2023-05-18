package com.huawei.config.testMethod;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 日志拦截器
 *
 * @author lkx
 */
@Slf4j
@Component
public class MethodInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String key= "traceId";
        TestMethod traceIdFollow;
        String traceId="";
        if (method.getDeclaringClass().isAnnotationPresent(TestMethod.class)) {
            traceIdFollow = method.getDeclaringClass().getAnnotation(TestMethod.class);
            traceId = traceIdFollow.projectName();
        }
        if (method.isAnnotationPresent(TestMethod.class)) {
            traceIdFollow = method.getAnnotation(TestMethod.class);
            traceId = traceIdFollow.projectName();
        }
        System.out.println(traceId);
        String headerTraceId = request.getHeader(key);
        if(StringUtils.isNotEmpty(headerTraceId)){
            //设置traceId
            MDC.put(key,headerTraceId);
        }else {
            MDC.put(key, traceId);
        }
        return true;
    }

}