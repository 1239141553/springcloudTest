package com.huawei.config.testSql;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * @author lkx
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface SqlLogs {
    /**
     * sql
     * @return
     */
   boolean taget() default false;
}
