package com.anread.book.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIChatConfig {
    @Value("${ai.chat.model}")
    private String model;
}
