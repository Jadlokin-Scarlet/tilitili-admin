package com.tilitili.admin.service.mirai;

import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.service.MiraiSessionService;
import com.tilitili.common.entity.mirai.MessageChain;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.manager.MiraiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RepeatHandle implements BaseMessageHandle {
    private final String KeyKey = "repeat.key";
    private final String valueKey = "repeat.value";
    private final String numberKey = "repeat.number";

    private final MiraiManager miraiManager;

    @Autowired
    public RepeatHandle(MiraiManager miraiManager) {
        this.miraiManager = miraiManager;
    }

    @Override
    public MessageHandleEnum getType() {
        return MessageHandleEnum.RepeatHandle;
    }

    @Override
    public MiraiMessage handleMessage(MiraiRequest request) {
        MiraiSessionService.MiraiSession session = request.getSession();
        MiraiMessageView message = request.getMessage();
        List<MessageChain> messageChainList = message.getMessageChain();
        String key = getKey(messageChainList);

        String oldKey = session.getOrDefault(KeyKey, "");
        int oldNumber = Integer.parseInt(session.getOrDefault(numberKey, "0"));
        if (oldKey.equals(key)) {
            session.put(numberKey, String.valueOf(oldNumber + 1));
        } else {
            session.put(KeyKey, key);
            session.put(numberKey, "1");
        }

        String newNumber = session.get(numberKey);
        if (Objects.equals(newNumber, "3")) {
            List<String> typeList = Arrays.asList("Plain", "Image");
            List<MessageChain> newMessageChainList = messageChainList.stream().filter(messageChain -> typeList.contains(message.getType())).collect(Collectors.toList());
            miraiManager.sendMessage(new MiraiMessage().setMessageType("List").setMessageChainList(newMessageChainList).setSendType("GroupMessage").setGroup(message.getSender().getGroup().getId()));
        }
        return null;
    }

    public String getKey(List<MessageChain> messageChainList) {
        return messageChainList.stream().map(messageChain -> {
            if (messageChain.getType().equals("Plain")) {
                return messageChain.getText();
            } else if (messageChain.getType().equals("Image")) {
                return messageChain.getImageId();
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.joining(","));
    }
}
