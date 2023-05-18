package com.huawei.config.testTraceId;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 将当前线程中的数据带入熔断线程
 *
 * @author lkx
 */
@Component
public class CustomizedHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    public CustomizedHystrixConcurrencyStrategy() {
        HystrixPlugins.reset();
        HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        Map<String, String> map = MDC.getCopyOfContextMap();
        return () -> {
            try {
                MDC.setContextMap(map);
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }
}

