package com.tilitili.admin.service.mirai;

import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.service.MiraiSessionService;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.manager.MiraiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Component
public class RepeatHandle implements BaseMessageHandle {
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
        String value = request.getText() + request.getUrl();
        MiraiMessage result = new MiraiMessage();

        String oldValue = session.getOrDefault(valueKey, "");
        int oldNumber = Integer.parseInt(session.getOrDefault(numberKey, "0"));
        if (oldValue.equals(value)) {
            session.put(numberKey, String.valueOf(oldNumber + 1));
        } else {
            session.put(valueKey, value);
            session.put(numberKey, "1");
        }

        String newNumber = session.get(numberKey);
        if (Objects.equals(newNumber, "3")) {
            miraiManager.sendGroupMessage("Plain", request.getText(), request.getMessage().getSender().getGroup().getId());
        }
        return null;
    }
}
