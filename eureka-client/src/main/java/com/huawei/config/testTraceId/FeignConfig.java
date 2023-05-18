package com.huawei.config.testTraceId;

import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

/**
 * Feign-headers,默认赋值
 *
 * @author lkx
 * @date 2021-06-10
 */
@Component
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(feign.RequestTemplate requestTemplate) {
        String key = "traceId";
        requestTemplate.header(key, MDC.get(key));

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                requestTemplate.header(name, values);

            }
        }
    }
}