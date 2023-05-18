package com.huawei.config.testTraceId;

import com.huawei.config.testMethod.MethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @description: 日志拦截器配置
 * @author: LKX
 * @date: 2021-6-21
 **/
@Configuration
public class TraceIdConfigurer extends WebMvcConfigurationSupport {

    @Autowired
    private TraceIdInterceptor traceIdInterceptor;
    @Autowired
    private MethodInterceptor methodInterceptor;
    /**
     * 注入自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置feign客户端对外路径
        registry.addInterceptor(traceIdInterceptor).addPathPatterns("/**");
        registry.addInterceptor( methodInterceptor).addPathPatterns("/**");
    }

}