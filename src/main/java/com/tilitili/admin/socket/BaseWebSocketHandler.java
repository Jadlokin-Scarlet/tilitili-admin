package com.tilitili.admin.socket;

import org.springframework.web.socket.handler.TextWebSocketHandler;

public class BaseWebSocketHandler extends TextWebSocketHandler {
    public String getUrl() {
        return null;
    }
}
