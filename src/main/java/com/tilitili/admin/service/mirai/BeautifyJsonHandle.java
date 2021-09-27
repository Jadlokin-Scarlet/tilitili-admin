package com.tilitili.admin.service.mirai;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.utils.Asserts;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class BeautifyJsonHandle implements BaseMessageHandle{
    @Override
    public MessageHandleEnum getType() {
        return MessageHandleEnum.BeautifyJsonHandle;
    }

    @Override
    public MiraiMessage handleMessage(MiraiRequest request) {
        MiraiMessage result = new MiraiMessage();
        String body = request.getBody();
        Asserts.notBlank(body, "格式错啦(内容)");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return result.setMessage(gson.toJson(gson.fromJson(body, Map.class))).setMessageType("Plain");
    }
}
