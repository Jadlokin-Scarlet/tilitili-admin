package com.tilitili.admin.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class BaseWebSocketHandler extends TextWebSocketHandler {
    public String getUrl() {
        return null;
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("handle pong message [{}]",message);
        Thread.sleep(60 * 1000);
        log.info("send ping message");
        session.sendMessage(new PingMessage());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected");
        log.info("send ping message");
        session.sendMessage(new PingMessage());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Transport Error", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Connection Closed [" + status.getReason() + "]");
    }
}
