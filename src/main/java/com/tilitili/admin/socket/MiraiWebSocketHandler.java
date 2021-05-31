package com.tilitili.admin.socket;

import com.google.gson.Gson;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class MiraiWebSocketHandler extends BaseWebSocketHandler {
    @Value("${mirai.base-url}")
    private String baseUrl;


    private final MiraiManager miraiManager;

    @Autowired
    public MiraiWebSocketHandler(MiraiManager miraiManager) {
        this.miraiManager = miraiManager;
    }

    @Override
    public String getUrl() {
        String sessionStr = miraiManager.auth();
        miraiManager.verify(sessionStr);
        return baseUrl.replace("http", "ws") + "message?sessionKey="+sessionStr;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            MiraiMessage miraiMessage = new Gson().fromJson(message.getPayload(), MiraiMessage.class);
            log.info("Message Received [{}]",miraiMessage);
            Asserts.notNull(miraiMessage, "未获取到消息");
            Asserts.notBlank(miraiMessage.getType(), "未获取到消息");
            Asserts.notNull(miraiMessage.getSender(), "未获取到消息");
            Asserts.notNull(miraiMessage.getMessageChain(), "未获取到消息");
            Asserts.notNull(miraiMessage.getSender().getId(), "未获取到消息");

            Asserts.isTrue(miraiMessage.getType().equals("FriendMessage"), "只支持私聊回复");
            String result = miraiManager.handleMessage(miraiMessage);
            Thread.sleep(1000);
            if (result.length() > 30) {
                result = result.substring(0, 25);
            }
            miraiManager.sendFriendMessage("Plain", result, miraiMessage.getSender().getId());
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error("异常", e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected");
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