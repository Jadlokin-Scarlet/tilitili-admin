package com.tilitili.admin.socket;

import com.google.gson.Gson;
import com.tilitili.admin.service.MiraiService;
import com.tilitili.admin.service.MiraiSessionService;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.entity.mirai.MiraiMessageViewRequest;
import com.tilitili.common.exception.AssertException;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.manager.ResourcesManager;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

@Slf4j
@Component
public class MiraiWebSocketHandler extends BaseWebSocketHandler {

    private final MiraiManager miraiManager;
    private final MiraiService miraiService;
    private final MiraiSessionService miraiSessionService;

    @Autowired
    public MiraiWebSocketHandler(MiraiManager miraiManager, MiraiService miraiService, MiraiSessionService miraiSessionService) {
        this.miraiManager = miraiManager;
        this.miraiService = miraiService;
        this.miraiSessionService = miraiSessionService;
    }

    @Override
    public String getUrl() {
        return miraiManager.getWebSocketUrl();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        MiraiMessage result;
        try {
            MiraiMessageViewRequest miraiMessageViewRequest = new Gson().fromJson(message.getPayload(), MiraiMessageViewRequest.class);
            log.info("Message Received [{}]",miraiMessageViewRequest);
            Asserts.notNull(miraiMessageViewRequest, "未获取到消息");
            Asserts.notNull(miraiMessageViewRequest.getData(), "未获取到消息");
            Asserts.notBlank(miraiMessageViewRequest.getData().getType(), "未获取到消息");
            Asserts.notNull(miraiMessageViewRequest.getData().getSender(), "未获取到消息");
            Asserts.notNull(miraiMessageViewRequest.getData().getMessageChain(), "未获取到消息");
            Asserts.notNull(miraiMessageViewRequest.getData().getSender().getId(), "未获取到消息");

            MiraiMessageView miraiMessage = miraiMessageViewRequest.getData();

//            Asserts.isFalse(miraiMessage.getType().equals("GroupMessage"), "不支持群聊回复");
            if (miraiMessage.getType().equals("GroupMessage")) {
                Long sender = miraiMessage.getSender().getGroup().getId();
                MiraiSessionService.MiraiSession miraiSession = miraiSessionService.getSession("group-" + sender);
                result = miraiService.handleGroupMessage(miraiMessage, miraiSession);
            } else {
                Long sender = miraiMessage.getSender().getId();
                MiraiSessionService.MiraiSession miraiSession = miraiSessionService.getSession("friend-" + sender);
                result = miraiService.handleMessage(miraiMessage, miraiSession);
            }
            Asserts.notBlank((""+result.getMessage()+result.getUrl()+result.getVoiceId()).replaceAll("null", ""), "回复为空");
            if (miraiMessage.getType().equals("FriendMessage")) {
                miraiManager.sendMessage(result.setSendType("friend").setQq(miraiMessage.getSender().getId()));
            } else if (miraiMessage.getType().equals("TempMessage")){
                miraiManager.sendMessage(result.setSendType("temp").setQq(miraiMessage.getSender().getId()).setGroup(miraiMessage.getSender().getGroup().getId()));
            } else {
                miraiManager.sendMessage(result.setSendType("group").setGroup(miraiMessage.getSender().getGroup().getId()));
            }
        } catch (AssertException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error("异常", e);
        }
    }

}