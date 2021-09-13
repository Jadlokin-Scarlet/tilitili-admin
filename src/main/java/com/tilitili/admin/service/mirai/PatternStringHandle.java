package com.tilitili.admin.service.mirai;

import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.utils.StringUtil;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.http.util.TextUtils.isBlank;

@Component
public class PatternStringHandle implements BaseMessageHandle{
    @Override
    public List<String> getKeyword() {
        return Arrays.asList("正则", "zz");
    }

    @Override
    public String getDescription() {
        return "正则匹配";
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
        String regex = request.getParam("r");
        String string = request.getParam("s");
        Asserts.notBlank(regex, "格式错啦(r)");
        Asserts.notBlank(string, "格式错啦(s)");
        List<String> pattenList = new ArrayList<>();
        pattenList.add(StringUtils.patten(regex, string));
        pattenList.addAll(StringUtils.extractList(regex, string));
        String patten = String.join("\n", pattenList);
        if (isBlank(patten)) {
            return result.setMessage("没匹配到").setMessageType("Plain");
        }
        return result.setMessage(patten).setMessageType("Plain");
    }
}
