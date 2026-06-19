package com.relatosdepapel.comms.config;

import com.relatosdepapel.comms.websocket.SupportWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SupportWebSocketHandler supportWebSocketHandler;

    public WebSocketConfig(SupportWebSocketHandler supportWebSocketHandler) {
        this.supportWebSocketHandler = supportWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(supportWebSocketHandler, "/ws/support")
                .setAllowedOriginPatterns("*");
    }
}
