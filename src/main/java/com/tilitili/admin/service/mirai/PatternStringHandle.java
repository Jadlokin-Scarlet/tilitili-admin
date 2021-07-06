package com.tilitili.admin.service.mirai;

import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.utils.Asserts;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PatternStringHandle implements BaseMessageHandle{
    @Override
    public List<String> getKeyword() {
        return Arrays.asList("正则", "zz");
    }

    @Override
    public String handleMessage(MiraiMessageView message, Map<String, String> map) {
        String regex = map.get("r");
        String string = map.get("s");
        Asserts.notBlank(regex, "格式错啦(r)");
        Asserts.notBlank(string, "格式错啦(s)");
        Matcher matcher = Pattern.compile(regex).matcher(string);
        List<String> result = new ArrayList<>();
        for (int i = 0; i <= matcher.groupCount(); i++){
            if (matcher.find(i)) {
                result.add(matcher.group(i));
            }
        }
        return String.join("\n", result);
    }
}
