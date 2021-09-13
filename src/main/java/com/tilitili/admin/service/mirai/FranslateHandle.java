package com.tilitili.admin.service.mirai;

import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.manager.BaiduManager;
import com.tilitili.common.utils.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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
    public List<String> getKeyword() {
        return Arrays.asList("翻译", "fy", "fy2");
    }

    @Override
    public String getDescription() {
        return "翻译文本或图片";
    }

    @Override
    public String getSendType() {
        return "friend";
    }

    @Override
    public Integer getType() {
        return 0;
    }


    @Override
    public MiraiMessage handleMessage(MiraiRequest request) {
        MiraiMessage result = new MiraiMessage();
        String body = request.getBody();
        String url = request.getUrl();
        String to = request.getParam("to");
        String text = request.getParam("t");
        Asserts.notBlank(body + url, "格式错啦(内容)");
        String cnText;
        if (to != null) {
            cnText = baiduManager.translate(to, text);
        } else if (isNotBlank(body)) {
            cnText = baiduManager.translate(body);
        } else {
            cnText = baiduManager.translateImage(url);
        }
        if (isBlank(cnText)) {
            return result.setMessage("无法翻译").setMessageType("Plain");
        }
        return result.setMessage(cnText).setMessageType("Plain");
    }
}
