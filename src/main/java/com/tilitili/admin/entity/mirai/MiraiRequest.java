package com.tilitili.admin.entity.mirai;

import com.tilitili.admin.service.MiraiSessionService;
import com.tilitili.common.entity.mirai.MessageChain;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.utils.StreamUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MiraiRequest {
    private final MiraiMessageView message;
    private final MiraiSessionService.MiraiSession session;
    private final Map<String, String> params;
    private final String text;
    private final String body;
    private final String url;
    private final String title;
    private final String[] textList;

    public MiraiRequest(MiraiMessageView message, MiraiSessionService.MiraiSession session) {
        this.message = message;
        this.session = session;

        List<MessageChain> messageChain = message.getMessageChain();
        text = messageChain.stream().filter(StreamUtil.isEqual(MessageChain::getType, "Plain")).map(MessageChain::getText).collect(Collectors.joining("\n"));
        url = messageChain.stream().filter(StreamUtil.isEqual(MessageChain::getType, "Image")).map(MessageChain::getUrl).findFirst().orElse("");
        textList = text.split("\n");
        
        if (session.containsKey("模式")) {
            title = session.get("模式");
            body = text;
//            if (Objects.equals(textList[0], "退出")) {
//                session.remove("模式");
//                return result.setMessage("停止"+title).setMessageType("Plain");
//            }
        } else {
            title = textList[0];
            body = Stream.of(textList).skip(1).collect(Collectors.joining("\n"));
//            if (textList[0].contains("模式")) {
//                String mod = textList[0].replaceAll("模式", "");
//                session.put("模式", mod);
//                return result.setMessage("开始"+mod).setMessageType("Plain");
//            }
        }

        String[] bodyList = body.split("\n");
        params = new HashMap<>();
        for (String line : bodyList) {
            if (!line.contains("=")) {
                continue;
            }
            String key = line.split("=")[0];
            String value = line.split("=")[1];
            params.put(key.trim(), value.trim());
        }
    }
    
    public String getParam(String key) {
        return params.get(key);
    }

    public String getParamOrDefault(String key, String or) {
        return params.getOrDefault(key, or);
    }

    public MiraiMessageView getMessage() {
        return message;
    }

    public MiraiSessionService.MiraiSession getSession() {
        return session;
    }

    public String getText() {
        return text;
    }

    public String getBody() {
        return body;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String[] getTextList() {
        return textList;
    }
}
