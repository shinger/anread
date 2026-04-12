// config/WebClientConfig.java
package com.anread.book.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ai.chat.api-key}")
    private String apiKey;

    @Value("${ai.chat.base-url}")
    private String baseURL;

    @Bean
    public WebClient deepSeekWebClient() {
        return WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                // 可在此添加拦截器、超时等
                .build();
    }
}