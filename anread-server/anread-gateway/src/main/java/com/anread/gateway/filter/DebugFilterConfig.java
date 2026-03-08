package com.anread.gateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

//@Configuration
public class DebugFilterConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)  // 最高优先级，最先执行
    public GlobalFilter debugFilter() {
        return (exchange, chain) -> {
            // 打印请求基本信息
            System.out.println("=== Debug Filter: Start ===");
            System.out.println("Request Path: " + exchange.getRequest().getPath());
            System.out.println("Request Origin: " + exchange.getRequest().getHeaders().getOrigin());
            System.out.println("Request Host: " + exchange.getRequest().getHeaders().getHost());
            System.out.println("=== Debug Filter: Before Chain ===");

            // 继续执行过滤器链，监听响应状态
            return chain.filter(exchange)
                    .doOnSuccess(v -> {
                        System.out.println("=== Debug Filter: Success ===");
                        System.out.println("Response Status: " + exchange.getResponse().getStatusCode());
                    })
                    .doOnError(e -> {
                        System.out.println("=== Debug Filter: Error ===");
                        System.out.println("Error: " + e.getMessage());
                    });
        };
    }
}