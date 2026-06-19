package com.relatosdepapel.comms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender,
                        @Value("${spring.mail.username:no-reply@relatosdepapel.com}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendOrderConfirmation(String to, String customerName, Long orderId,
                                      BigDecimal total, int itemCount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Relatos de Papel · Pedido #" + orderId + " confirmado");
        message.setText("""
                Hola %s:

                ¡Gracias por tu compra en Relatos de Papel!
                Tu pedido #%d ha sido aceptado y se está procesando.

                Resumen:
                  · Artículos: %d
                  · Total: %.2f €

                Recibirás novedades sobre el estado de tu envío.

                Un saludo,
                El equipo de Relatos de Papel
                """.formatted(customerName, orderId, itemCount, total));

        mailSender.send(message);
        log.info("Email de confirmación enviado a {} para el pedido {}", to, orderId);
    }
}
