package com.tilitili.admin.service.mirai;

import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String getSendType() {
        return "friend";
    }

    @Override
    public MiraiMessage handleMessage(MiraiRequest request) {
        MiraiMessage result = new MiraiMessage();
        StringBuilder stringBuilder = new StringBuilder("咱可以帮你做这些事！\n");
        String body = handleList.stream().filter(a->!a.getKeyword().isEmpty()).sorted(Comparator.comparing(a -> a.getKeyword().size(), Comparator.reverseOrder())).map(handle ->
                String.join(",", handle.getKeyword()) + "：" + handle.getDescription()
        ).collect(Collectors.joining("\n"));
        return result.setMessage(stringBuilder.append(body).toString()).setMessageType("Plain");
    }
}
