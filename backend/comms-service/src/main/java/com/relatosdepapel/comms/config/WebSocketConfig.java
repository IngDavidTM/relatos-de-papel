package com.relatosdepapel.comms.config;

import com.relatosdepapel.comms.websocket.SupportWebSocketHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SupportWebSocketHandler supportWebSocketHandler;
    private final List<String> allowedOrigins;

    public WebSocketConfig(
            SupportWebSocketHandler supportWebSocketHandler,
            @Value("${app.cors.allowed-origins:*}") String allowedOrigins) {
        this.supportWebSocketHandler = supportWebSocketHandler;
        this.allowedOrigins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(supportWebSocketHandler, "/ws/support")
                .setAllowedOriginPatterns(allowedOrigins.toArray(String[]::new));
    }
}
