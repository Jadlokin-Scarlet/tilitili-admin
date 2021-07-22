package com.tilitili.admin.service.mirai;

import com.tilitili.admin.utils.StringUtil;
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
    public String handleMessage(MiraiMessageView message, Map<String, String> map) {
        String regex = map.get("r");
        String string = map.get("s");
        Asserts.notBlank(regex, "格式错啦(r)");
        Asserts.notBlank(string, "格式错啦(s)");
        List<String> resultList = new ArrayList<>();
        resultList.add(StringUtils.patten(regex, string));
        resultList.addAll(StringUtils.extractList(regex, string));
        String result = String.join("\n", resultList);
        if (isBlank(result)) {
            return "没匹配到";
        }
        return result;
    }
}
