package com.tilitili.admin.service;

import com.tilitili.admin.service.mirai.BaseMessageHandle;
import com.tilitili.common.entity.mirai.MessageChain;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.exception.AssertException;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.manager.ResourcesManager;
import com.tilitili.common.utils.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Slf4j
@Service
public class MiraiService {

    private final List<BaseMessageHandle> messageHandleList;
    private final ResourcesManager resourcesManager;
    private final MiraiManager miraiManager;

    @Autowired
    public MiraiService(List<BaseMessageHandle> messageHandleList, ResourcesManager resourcesManager, MiraiManager miraiManager) {
        this.messageHandleList = messageHandleList;
        this.resourcesManager = resourcesManager;
        this.miraiManager = miraiManager;
    }

    public MiraiMessage handleGroupMessage(MiraiMessageView message, MiraiSessionService.MiraiSession miraiSession) {
        MiraiMessage result = new MiraiMessage();
        List<MessageChain> messageChain = message.getMessageChain();
        String text = messageChain.stream().filter(StreamUtil.isEqual(MessageChain::getType, "Plain")).map(MessageChain::getText).collect(Collectors.joining("\n"));
        String url = messageChain.stream().filter(StreamUtil.isEqual(MessageChain::getType, "Image")).map(MessageChain::getUrl).findFirst().orElse("");
        String value = text+url;

        String oldValue = miraiSession.getOrDefault("value", "");
        int oldNumber = Integer.parseInt(miraiSession.getOrDefault("number", "0"));
        if (oldValue.equals(value)) {
            miraiSession.put("number", String.valueOf(oldNumber + 1));
        } else {
            miraiSession.put("value", value);
            miraiSession.put("number", "1");
        }

        String newNumber = miraiSession.get("number");
        if (Objects.equals(newNumber, "3") && value.length() < 10) {
            return result.setMessage(text).setMessageType("Plain");
        }
        return result.setMessage("").setMessageType("Plain");
    }

    public MiraiMessage handleMessage(MiraiMessageView message, MiraiSessionService.MiraiSession miraiSession) {
        MiraiMessage result = new MiraiMessage();
        try {
            List<MessageChain> messageChain = message.getMessageChain();
            String text = messageChain.stream().filter(StreamUtil.isEqual(MessageChain::getType, "Plain")).map(MessageChain::getText).collect(Collectors.joining("\n"));
            String url = messageChain.stream().filter(StreamUtil.isEqual(MessageChain::getType, "Image")).map(MessageChain::getUrl).findFirst().orElse("");
            String[] textList = text.split("\n");

            if (message.getType().equals("TempMessage")) {
                if (resourcesManager.isForwardTempMessage()) {
                    miraiManager.sendFriendMessage("Plain", text);
                }
            }

            String title;
            String body;
            if (miraiSession.containsKey("模式")) {
                title = miraiSession.get("模式");
                body = text;
                if (Objects.equals(textList[0], "退出")) {
                    miraiSession.remove("模式");
                    return result.setMessage("停止"+title).setMessageType("Plain");
                }
            } else {
                title = textList[0];
                body = Stream.of(textList).skip(1).collect(Collectors.joining("\n"));
                if (textList[0].contains("模式")) {
                    String mod = textList[0].replaceAll("模式", "");
                    miraiSession.put("模式", mod);
                    return result.setMessage("开始"+mod).setMessageType("Plain");
                }
            }

            String[] bodyList = body.split("\n");
            Map<String, String> map = new HashMap<>();
            for (String line : bodyList) {
                if (!line.contains("=")) {
                    continue;
                }
                String key = line.split("=")[0];
                String value = line.split("=")[1];
                map.put(key.trim(), value.trim());
            }
            if (isNotBlank(body)) {
                map.put("body", body);
            }
            if (isNotBlank(url)) {
                map.put("url", url);
            }

            for (BaseMessageHandle messageHandle : messageHandleList) {
                if (messageHandle.getKeyword().contains(title)) {
                    return messageHandle.handleMessage(message, map);
                }
            }

            return result.setMessage("?").setMessageType("Plain");
        } catch (AssertException e) {
            log.error(e.getMessage());
            return result.setMessage(e.getMessage()).setMessageType("Plain");
        } catch (Exception e) {
            log.error("处理消息回调失败",e);
            return result.setMessage("¿").setMessageType("Plain");
        }
    }
}
