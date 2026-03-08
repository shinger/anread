package com.anread.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String path = uri.getPath();
        String query = uri.getQuery() != null ? uri.getQuery() : "";

        // 记录请求基本信息
        log.info(">>> Incoming Request: {} {}", request.getMethod(), path);
        if (!query.isEmpty()) {
            log.info(">>> Query Params: {}", query);
        }

        // 可选：记录请求头（如需）
        // request.getHeaders().forEach((key, values) -> log.debug("Header: {}={}", key, values));

        // 包装响应，以便捕获响应体
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);

                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        try {
                            // 读取原始响应数据
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);

                            // 将字节数组转为字符串（假设是 UTF-8）
                            String responseBody = new String(content, StandardCharsets.UTF_8);
                            log.info("<<< Response Body: {}", responseBody);

                            // 重要：释放原始 DataBuffer，避免内存泄漏
                            DataBufferUtils.release(dataBuffer);

                            // 返回新的 DataBuffer 给下游
                            return bufferFactory.wrap(content);
                        } catch (Exception e) {
                            log.error("Error reading response body", e);
                            DataBufferUtils.release(dataBuffer);
                            throw new RuntimeException(e);
                        }
                    }));
                }
                // 非 Flux 情况（罕见），直接写回
                return super.writeWith(body);
            }
        };

        // 替换响应对象，继续过滤链
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        // 设置为较高优先级（负数更早执行，但这里我们关注响应，所以靠后执行更好）
        // HIGHEST_PRECEDENCE = -2147483648, LOWEST_PRECEDENCE = 2147483647
        // 我们希望在所有业务逻辑之后、响应写出之前拦截，所以用 Ordered.LOWEST_PRECEDENCE - 1
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}