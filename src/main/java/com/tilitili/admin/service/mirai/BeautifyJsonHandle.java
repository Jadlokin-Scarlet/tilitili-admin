package com.tilitili.admin.service.mirai;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.utils.Asserts;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class BeautifyJsonHandle implements BaseMessageHandle{

    @Override
    public List<String> getKeyword() {
        return Arrays.asList("Json", "json");
    }

    @Override
    public String handleMessage(MiraiMessageView message, Map<String, String> map) {
        String body = map.getOrDefault("body", "");
        Asserts.notBlank(body, "格式错啦(内容)");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(gson.fromJson(body, Map.class));
    }
}
