package com.anread.admin.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public final static String EXCHANGE_BOOK_SPLIT = "book.split.exchange";

    public final static String QUEUE_BOOK_SPLIT = "book.split.queue";

    public final static String ROUTING_BOOK_SPLIT = "book.split.routing";

    @Bean
    public Queue demoQueue() {
        return new Queue(QUEUE_BOOK_SPLIT);
    }

    @Bean
    TopicExchange demoExchange() {
        return new TopicExchange(EXCHANGE_BOOK_SPLIT);
    }

    @Bean
    public Binding binding(Queue demoQueue, TopicExchange demoExchange) {
        return BindingBuilder.bind(demoQueue).to(demoExchange).with(ROUTING_BOOK_SPLIT);
    }

}
