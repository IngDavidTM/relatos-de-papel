package com.relatosdepapel.comms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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

    @Bean
    TopicExchange relatosEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue orderCreatedQueue() {
        return new Queue(QUEUE_ORDER_CREATED, true);
    }

    @Bean
    Binding orderCreatedBinding(Queue orderCreatedQueue, TopicExchange relatosEventsExchange) {
        return BindingBuilder.bind(orderCreatedQueue)
                .to(relatosEventsExchange)
                .with(ROUTING_KEY_ORDER_CREATED);
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
