package com.relatosdepapel.comms.event;

import com.relatosdepapel.comms.client.UsersClient;
import com.relatosdepapel.comms.client.dto.UserResponse;
import com.relatosdepapel.comms.config.RabbitMQConfig;
import com.relatosdepapel.comms.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Procesa de forma asíncrona los eventos "pedido creado": resuelve el email
 * del cliente contra users-service y le envía la confirmación de la compra.
 */
@Component
public class OrderCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final UsersClient usersClient;
    private final EmailService emailService;

    public OrderCreatedListener(UsersClient usersClient, EmailService emailService) {
        this.usersClient = usersClient;
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_CREATED)
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Evento order.created recibido para el pedido {}", event.orderId());
        try {
            UserResponse user = usersClient.getById(event.userId());
            emailService.sendOrderConfirmation(
                    user.email(),
                    user.name(),
                    event.orderId(),
                    event.totalAmount(),
                    event.itemCount());
        } catch (Exception ex) {
            log.error("No se pudo notificar el pedido {}: {}", event.orderId(), ex.getMessage());
        }
    }
}
