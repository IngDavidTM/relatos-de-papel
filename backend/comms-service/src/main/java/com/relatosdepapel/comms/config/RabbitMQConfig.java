package com.relatosdepapel.comms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topología de mensajería para el consumo de eventos.
 * Debe ser compatible con la que publica orders-service.
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "relatos.events";
    public static final String ROUTING_KEY_ORDER_CREATED = "order.created";
    public static final String QUEUE_ORDER_CREATED = "comms.order-created";
    public static final String DEAD_LETTER_EXCHANGE = "relatos.events.dlx";
    public static final String DEAD_LETTER_QUEUE = "comms.order-created.dlq";
    public static final String DEAD_LETTER_ROUTING_KEY = "order.created.failed";

    @Bean
    TopicExchange relatosEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue orderCreatedQueue() {
        return QueueBuilder.durable(QUEUE_ORDER_CREATED)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    TopicExchange deadLetterExchange() {
        return new TopicExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    Queue orderCreatedDeadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    Binding orderCreatedBinding(
            @Qualifier("orderCreatedQueue") Queue orderCreatedQueue,
            @Qualifier("relatosEventsExchange") TopicExchange relatosEventsExchange) {
        return BindingBuilder.bind(orderCreatedQueue)
                .to(relatosEventsExchange)
                .with(ROUTING_KEY_ORDER_CREATED);
    }

    @Bean
    Binding orderCreatedDeadLetterBinding(
            @Qualifier("orderCreatedDeadLetterQueue") Queue deadLetterQueue,
            @Qualifier("deadLetterExchange") TopicExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(DEAD_LETTER_ROUTING_KEY);
    }

    /**
     * Al declarar un único MessageConverter, Spring Boot lo aplica
     * automáticamente tanto al RabbitTemplate como a la factory de listeners.
     */
    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
