package com.huawei.requestHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.RequestPath;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class IntercepterConfig implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //拦截方法，拦截到请求后，自动执行
        log.info("filter 拦截方法，拦截到请求后，自动执行 ");
        //以后开发中，不会将 user对象存到session，只会在地址上带上token
        //根据token是否有空可以判断是否登录
        //http://localhost:8001/users/3?token=10001&pageSize=30
        RequestPath path = exchange.getRequest().getPath();
        System.out.println(path);
        if(path.toString().contains("no_session")){
            return chain.filter(exchange);//2 全部放行
        }else {
            throw new RuntimeException("没有权限请登录");
        }

    }

    @Override
    public int getOrder() {
        //3:系统调用该方法获得过滤器的优先级
        return 1; //数字越小，越优先
    }
}

