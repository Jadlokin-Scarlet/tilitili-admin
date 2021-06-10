package com.tilitili.admin.socket;

import com.google.gson.Gson;
import com.tilitili.admin.service.MiraiService;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

@Slf4j
@Component
public class MiraiWebSocketHandler extends BaseWebSocketHandler {
    @Value("${mirai.base-url}")
    private String baseUrl;

    private final MiraiManager miraiManager;
    private final MiraiService miraiService;

    @Autowired
    public MiraiWebSocketHandler(MiraiManager miraiManager, MiraiService miraiService) {
        this.miraiManager = miraiManager;
        this.miraiService = miraiService;
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
            MiraiMessageView miraiMessage = new Gson().fromJson(message.getPayload(), MiraiMessageView.class);
            log.info("Message Received [{}]",miraiMessage);
            Asserts.notNull(miraiMessage, "未获取到消息");
            Asserts.notBlank(miraiMessage.getType(), "未获取到消息");
            Asserts.notNull(miraiMessage.getSender(), "未获取到消息");
            Asserts.notNull(miraiMessage.getMessageChain(), "未获取到消息");
            Asserts.notNull(miraiMessage.getSender().getId(), "未获取到消息");

            Asserts.isFalse(miraiMessage.getType().equals("GroupMessage"), "不支持群聊回复");
            String result = miraiService.handleMessage(miraiMessage);
            Thread.sleep(1000);
            if (result.length() > 100) {
                result = result.substring(0, 90);
            }
            if (miraiMessage.getType().equals("FriendMessage")) {
                miraiManager.sendFriendMessage("Plain", result, miraiMessage.getSender().getId());
            } else if (miraiMessage.getType().equals("TempMessage")){
                miraiManager.sendTempMessage("Plain", result, miraiMessage.getSender().getGroup().getId(), miraiMessage.getSender().getId());
            }
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error("异常", e);
        }
    }

}