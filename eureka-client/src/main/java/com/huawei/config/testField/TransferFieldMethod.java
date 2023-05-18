package com.huawei.config.testField;

import java.lang.annotation.*;

/**
 * TraceId 跟踪器
 *
 * @author lkx
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TransferFieldMethod {
    String target() default "";
}