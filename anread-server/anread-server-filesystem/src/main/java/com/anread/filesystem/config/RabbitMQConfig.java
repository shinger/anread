package com.anread.filesystem.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public final static String EXCHANGE_BOOK_SPLIT = "book.split.exchange";

    public final static String QUEUE_BOOK_SPLIT = "book.split.queue";

    public final static String ROUTING_BOOK_SPLIT = "book.split.routing";

    @Bean
    public Queue bookSplitQueue() {
        return QueueBuilder.durable(QUEUE_BOOK_SPLIT)
                .withArgument("x-dead-letter-exchange", "dlx.exchange") // 指定死信交换机
                .withArgument("x-dead-letter-routing-key", "dlq.routing.key")
                .build();
    }

    @Bean
    TopicExchange bookSplitExchange() {
        return new TopicExchange(EXCHANGE_BOOK_SPLIT);
    }

    @Bean
    public Binding binding(@Qualifier("bookSplitQueue")Queue bookSplitQueue, TopicExchange bookSplitExchange) {
        return BindingBuilder.bind(bookSplitQueue).to(bookSplitExchange).with(ROUTING_BOOK_SPLIT);
    }

    // 死信交换机 & 队列
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange("dlx.exchange");
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("dead.letter.queue").build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(dlxExchange()).with("dlq.routing.key");
    }

}
