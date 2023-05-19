package huawei.KeyResolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * @Title:
 * @Auther:
 * @Date: 2019/8/28 17:13
 * @Version: 1.0
 * @Description:
 */
@Configuration
public class RequestRateLimiterConfig {

    @Bean
    @Primary
    KeyResolver apiKeyResolver() {
        //按URL限流,即以每秒内请求数按URL分组统计，超出限流的url请求都将返回429状态
        return exchange -> Mono.just(exchange.getRequest().getPath().toString());
    }

    @Bean
    KeyResolver userKeyResolver() {
        //按用户限流
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    }

    @Bean
    KeyResolver ipKeyResolver() {
        //按IP来限流
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

}