package com.tilitili.admin.service.mirai;

import com.tilitili.common.entity.mirai.MiraiMessageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class HelpHandle implements BaseMessageHandle {
    private final List<BaseMessageHandle> handleList;

    @Autowired
    public HelpHandle(List<BaseMessageHandle> handleList) {
        this.handleList = handleList;
    }

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
        StringBuilder stringBuilder = new StringBuilder("咱可以帮你做这些事！").append("\n");
        for (BaseMessageHandle handle : handleList) {
            stringBuilder.append(String.join(",", handle.getKeyword())).append("：").append(handle.getDescription()).append("\n");
        }
        return stringBuilder.toString();
    }
}
