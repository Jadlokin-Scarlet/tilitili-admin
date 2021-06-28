package com.tilitili.admin.socket;

import com.tilitili.common.utils.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BaseWebSocketHandler extends TextWebSocketHandler {
    public String getUrl() {
        return null;
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) {
        log.info("接收到pong消息");
        sleepAndPing(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("连接websocket成功，url={}", getUrl());
        sleepAndPing(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws IOException {
        log.error("websocket网络异常", exception);
        session.close(CloseStatus.SERVER_ERROR);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("连接关闭，reason={}", status.getReason());
        super.afterConnectionClosed(session, status);
    }

    private void sleepAndPing(WebSocketSession session) {
        Executors.newSingleThreadScheduledExecutor().schedule(StreamUtil.tryRun(() -> {
            log.info("发送ping消息");
            session.sendMessage(new PingMessage());
        }), 9, TimeUnit.MINUTES);
    }
}
