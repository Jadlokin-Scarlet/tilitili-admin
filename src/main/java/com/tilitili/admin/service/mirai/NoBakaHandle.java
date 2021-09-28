package com.tilitili.admin.service.mirai;

import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.service.MiraiSessionService;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class NoBakaHandle implements BaseMessageHandle {
    private final MiraiManager miraiManager;

    @Autowired
    public NoBakaHandle(MiraiManager miraiManager) {
        this.miraiManager = miraiManager;
    }

    @Override
    public MessageHandleEnum getType() {
        return MessageHandleEnum.NoBakaHandle;
    }

    @Override
    public MiraiMessage handleMessage(MiraiRequest request) throws Exception {
        String text = request.getText();
        MiraiMessage result = new MiraiMessage();

//        int bdCount = StringUtils.findCount("笨蛋", text);
//        if (bdCount > 0) {
//            String repeat = IntStream.range(0, bdCount).mapToObj(c -> "不笨").collect(Collectors.joining());
//            miraiManager.sendGroupMessage("Plain", repeat, request.getMessage().getSender().getGroup().getId());
//            return result.setMessage("").setMessageType("Plain");
//        }

        int ddCount = StringUtils.findCount("dd", text);
        if (ddCount > 0) {
            String repeat = IntStream.range(0, ddCount).mapToObj(c -> "bd").collect(Collectors.joining());
            miraiManager.sendGroupMessage("Plain", repeat, request.getMessage().getSender().getGroup().getId());
            return result.setMessage("").setMessageType("Plain");
        }
        return null;
    }
}
