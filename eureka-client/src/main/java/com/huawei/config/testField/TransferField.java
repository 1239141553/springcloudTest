package com.huawei.config.testField;

import java.lang.annotation.*;

/**
 * TraceId 跟踪器
 *
 * @author lkx
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TransferField {
    String target() default "";
}