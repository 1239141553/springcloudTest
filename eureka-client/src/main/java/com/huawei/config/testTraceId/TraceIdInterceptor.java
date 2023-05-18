package com.huawei.config.testTraceId;

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
public class TraceIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        log.info("进入traceId拦截器");
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String key= "traceId";
        TraceIdFollow traceIdFollow;
        String traceId="";
        if (method.getDeclaringClass().isAnnotationPresent(TraceIdFollow.class)) {
            traceIdFollow = method.getDeclaringClass().getAnnotation(TraceIdFollow.class);
            traceId = traceIdFollow.projectName();
        }
        if (method.isAnnotationPresent(TraceIdFollow.class)) {
            traceIdFollow = method.getAnnotation(TraceIdFollow.class);
            traceId = traceIdFollow.projectName();
        }
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