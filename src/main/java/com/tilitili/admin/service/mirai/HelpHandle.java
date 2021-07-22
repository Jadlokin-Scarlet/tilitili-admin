package com.tilitili.admin.service.mirai;

import com.tilitili.common.entity.mirai.MiraiMessageView;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class HelpHandle implements BaseMessageHandle {
    @Override
    public List<String> getKeyword() {
        return Arrays.asList("帮助", "help", "?");
    }

    @Override
    public String getDescription() {
        return "获取帮助";
    }

    @Override
    public String handleMessage(MiraiMessageView message, Map<String, String> map) {
        return "咱可以帮你做这些事！" +
                "1.";
    }
}
