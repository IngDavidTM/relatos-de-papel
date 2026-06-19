package com.relatosdepapel.comms.websocket;

import com.relatosdepapel.comms.service.GeminiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Maneja la conexión WebSocket del chat de soporte. Cada mensaje del usuario
 * se reenvía al agente de IA (Gemini) y la respuesta se devuelve por el mismo
 * canal.
 */
@Component
public class SupportWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(SupportWebSocketHandler.class);

    private final GeminiService geminiService;

    public SupportWebSocketHandler(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Conexión de soporte establecida: {}", session.getId());
        session.sendMessage(new TextMessage(
                "¡Hola! Soy Papelito, tu asistente de Relatos de Papel. ¿En qué puedo ayudarte?"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String userMessage = message.getPayload();
        log.debug("Mensaje de soporte [{}]: {}", session.getId(), userMessage);
        String reply = geminiService.ask(userMessage);
        session.sendMessage(new TextMessage(reply));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Conexión de soporte cerrada: {} ({})", session.getId(), status);
    }
}
