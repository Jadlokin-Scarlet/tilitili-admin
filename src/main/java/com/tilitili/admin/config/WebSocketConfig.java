package com.tilitili.admin.config;

import com.tilitili.admin.socket.BaseWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Configuration
public class WebSocketConfig {
    final List<BaseWebSocketHandler> webSocketHandlerList;

    @Autowired
    public WebSocketConfig(List<BaseWebSocketHandler> webSocketHandlerList) {
        this.webSocketHandlerList = webSocketHandlerList;
    }

    @PostConstruct
    public void webSocketConnectionManager() {
        for (BaseWebSocketHandler webSocketHandler : webSocketHandlerList) {
            try {
                StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
                WebSocketConnectionManager webSocketConnectionManager = new WebSocketConnectionManager(standardWebSocketClient, webSocketHandler, webSocketHandler.getUrl());
                webSocketConnectionManager.setAutoStartup(true);
                webSocketConnectionManager.start();
                webSocketHandler.setWebSocketConnectionManager(webSocketConnectionManager);
            } catch (Exception e) {
                log.error("异常", e);
            }
        }
    }
}
