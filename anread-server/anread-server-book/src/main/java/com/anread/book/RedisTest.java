package com.anread.book;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class RedisTest {
    public static void main(String[] args) {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        connectionFactory.afterPropertiesSet();

        ReactiveRedisTemplate<String, String> template = new ReactiveRedisTemplate<>(connectionFactory,
                RedisSerializationContext.string());

        Mono<Boolean> set = template.opsForValue().set("foo1", "bar");
//        set.block(Duration.ofSeconds(10));


        connectionFactory.destroy();
    }
}
