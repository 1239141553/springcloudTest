package com.huawei.config.testMethod;

import com.huawei.config.testTraceId.TraceIdConfigurer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TraceId 跟踪器
 *
 * @author lkx
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(TraceIdConfigurer.class)
public @interface TestMethod {
    String projectName() default "";
}