package com.tilitili.admin.service.mirai;

import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.manager.BaiduManager;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Component
public class FranslateHandle implements BaseMessageHandle{

    private final BaiduManager baiduManager;

    @Autowired
    public FranslateHandle(BaiduManager baiduManager) {
        this.baiduManager = baiduManager;
    }

    @Override
    public MessageHandleEnum getType() {
        return MessageHandleEnum.FranslateHandle;
    }

    @Override
    public MiraiMessage handleMessage(MiraiRequest request) {
        MiraiMessage result = new MiraiMessage();
        String body = request.getBody();
        String url = request.getUrl();
        String to = request.getParam("to");
        String text = request.getParamOrDefault("t", "");
        Asserts.notBlank(body + url + text, "格式错啦(内容)");

        String enText = StringUtils.convertToSmallWord(text + body);
        String cnText;
        if (to != null) {
            cnText = baiduManager.translate(to, enText);
        } else if (isNotBlank(body)) {
            cnText = baiduManager.translate(enText);
        } else {
            cnText = baiduManager.translateImage(url);
        }
        if (isBlank(cnText)) {
            return result.setMessage("无法翻译").setMessageType("Plain");
        }
        return result.setMessage(cnText).setMessageType("Plain");
    }
}
