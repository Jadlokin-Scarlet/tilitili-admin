package com.tilitili.admin.service.mirai;

import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.service.MiraiSessionService;
import com.tilitili.common.entity.mirai.MiraiMessage;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.apache.http.util.TextUtils.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Component
public class RepeatHandle implements BaseMessageHandle {
    @Override
    public List<String> getKeyword() {
        return Collections.emptyList();
    }

    @Override
    public String getDescription() {
        return "复读。";
    }

    @Override
    public String getSendType() {
        return "group";
    }

    @Override
    public MiraiMessage handleMessage(MiraiRequest request) {
        MiraiSessionService.MiraiSession session = request.getSession();
        String value = request.getText() + request.getUrl();
        MiraiMessage result = new MiraiMessage();

        String oldValue = session.getOrDefault("value", "");
        int oldNumber = Integer.parseInt(session.getOrDefault("number", "0"));
        if (oldValue.equals(value)) {
            session.put("number", String.valueOf(oldNumber + 1));
        } else {
            session.put("value", value);
            session.put("number", "1");
        }

        String newNumber = session.get("number");
        if (Objects.equals(newNumber, "3")) {
            boolean hasImage = isNotBlank(request.getUrl());
            return result.setMessage(request.getText()).setMessageType(hasImage? "ImageText": "Plain").setUrl(request.getUrl());
        }
        return result.setMessage("").setMessageType("Plain");
    }
}
