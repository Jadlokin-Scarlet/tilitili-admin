package com.tilitili.admin.socket;

import com.tilitili.common.utils.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BaseWebSocketHandler implements WebSocketHandler {

    private WebSocketConnectionManager webSocketConnectionManager;

    public String getUrl() {
        return null;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("连接websocket成功，url={}", getUrl());
        sleepAndPing(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            handleTextMessage(session, (TextMessage) message);
        } else if (message instanceof PongMessage) {
            sleepAndPing(session);
        } else {
            log.error("Unexpected WebSocket message type: " + message);
        }
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws IOException {
        log.error("websocket网络异常", exception);
        session.close(CloseStatus.SERVER_ERROR);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("连接关闭，reason={}", status.getReason());
        webSocketConnectionManager.stop();
        webSocketConnectionManager.start();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    }

    public void setWebSocketConnectionManager(WebSocketConnectionManager webSocketConnectionManager) {
        this.webSocketConnectionManager = webSocketConnectionManager;
    }

    @Async
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void heartBeat() throws Exception {
        log.error("连接断开，重连");
        webSocketConnectionManager.stopInternal();
        webSocketConnectionManager.startInternal();
    }

    private void sleepAndPing(WebSocketSession session) {
        Executors.newSingleThreadScheduledExecutor().schedule(StreamUtil.tryRun(() -> {
            log.info("发送ping消息");
            session.sendMessage(new PingMessage());
        }), 30, TimeUnit.MINUTES);
    }
}
