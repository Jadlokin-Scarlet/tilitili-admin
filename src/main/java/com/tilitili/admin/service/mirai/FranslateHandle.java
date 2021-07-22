package com.tilitili.admin.service.mirai;

import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.manager.BaiduManager;
import com.tilitili.common.utils.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        return Arrays.asList("翻译", "fy");
    }

    @Override
    public String getDescription() {
        return "翻译文本或图片";
    }

    @Override
    public String handleMessage(MiraiMessageView message, Map<String, String> map) {
        String body = map.getOrDefault("body", "");
        String url = map.getOrDefault("url", "");
        Asserts.notBlank(body + url, "格式错啦(内容)");
        String result;
        if (isNotBlank(body)) {
            result = baiduManager.translate(body);
        } else {
            result = baiduManager.translateImage(url);
        }
        if (isBlank(result)) {
            return "无法翻译";
        }
        return result;
    }
}
