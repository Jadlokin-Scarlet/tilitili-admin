package com.tilitili.admin.socket;

import com.tilitili.admin.service.MiraiService;
import com.tilitili.admin.service.MiraiSessionService;
import com.tilitili.common.manager.MiraiManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

@Slf4j
@Component
public class MiraiWebSocketHandler extends BaseWebSocketHandler {

    private final MiraiManager miraiManager;
    private final MiraiService miraiService;

    @Autowired
    public MiraiWebSocketHandler(MiraiManager miraiManager, MiraiService miraiService) {
        this.miraiManager = miraiManager;
        this.miraiService = miraiService;
    }

    @Override
    public String getUrl() {
        return miraiManager.getWebSocketUrl();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        miraiService.syncHandleTextMessage(session, message);
    }

}