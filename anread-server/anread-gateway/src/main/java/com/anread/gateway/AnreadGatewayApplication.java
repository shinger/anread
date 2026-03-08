package com.anread.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import java.util.function.Function;

@SpringBootApplication
@EnableDiscoveryClient
public class AnreadGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnreadGatewayApplication.class, args);
    }

    @Bean
    public Function<String, String> uppercase() {
        return v -> v.toUpperCase();
    }

    @Bean
    public Function<String, String> concat() {
        return v -> v + v;
    }

}
