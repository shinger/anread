package com.anread.gateway.filter;

import com.anread.common.utils.JWTUtil;
import com.anread.common.utils.ThreadLocalUtil;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JWTFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 登录和注册接口，自动放行
        String path = exchange.getRequest().getPath().toString();
        if (path.contains("/user/login") || path.contains("/user/register")) {
            return chain.filter(exchange);
        }

        // 测试接口，自动放行
        if (exchange.getRequest().getHeaders().getFirst("Admin-Test") != null) {
            return chain.filter(exchange);
        }

        // 其他接口，校验token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        // 没有token，自动放行
        if (token != null) {
            // 获取请求头中令牌
            try {
                // 验证令牌
                JWTUtil.verify(token);
                String userId = JWTUtil.parseId(token);

                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-ID", userId).build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            } catch (SignatureVerificationException e) {
                log.error("无效签名: {}", e.getMessage());
            } catch (TokenExpiredException e) {
                log.error("token过期: {}", e.getMessage());
            } catch (AlgorithmMismatchException e) {
                log.error("算法不一致: {}", e.getMessage());
            } catch (Exception e) {
                log.error("token无效: {}", e.getMessage());
            }
        }

        // Token校验失败，拒绝访问
        log.warn("JWT校验失败，token: {}", token);
        Map<String, Object> map = new HashMap<>();
        map.put("code", "401");  // 设置状态
        map.put("message", "登录已过期！");

        try {
            String json = new ObjectMapper().writeValueAsString(map);
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentLength(json.getBytes(StandardCharsets.UTF_8).length);
            DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer))
                    .then(Mono.defer(response::setComplete));
        } catch (JsonProcessingException e) {
            log.error("JSON返回值序列化失败: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
