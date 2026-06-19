package com.relatosdepapel.orders.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topología de mensajería para la publicación de eventos de pedidos.
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "relatos.events";
    public static final String ROUTING_KEY_ORDER_CREATED = "order.created";

    @Bean
    TopicExchange relatosEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
