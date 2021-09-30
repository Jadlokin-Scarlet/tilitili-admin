package com.tilitili.admin.emnus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MessageHandleEnum {

    AddRecommendHandle("AddRecommendHandle", Arrays.asList("推荐", "tj"), "添加推荐", "friend", 0, 0),
    AddSubscriptionHandle("AddSubscriptionHandle", Arrays.asList("关注", "gz"), "关注b站up，关注后可以获得开播提醒和动态推送", "friend", 0, 0),
    BeautifyJsonHandle("BeautifyJsonHandle", Arrays.asList("Json", "json"), "Json美化", "friend", 0, 0),
    CalendarHandle("CalendarHandle", Arrays.asList("日程表", "rc"), "日程表（xxx时间叫我xxx）", "friend", 0, 0),
    FindImageHandle("FindImageHandle", Arrays.asList("找图", "zt"), "查找原图", "friend", 0, 0),
    FranslateHandle("FranslateHandle", Arrays.asList("翻译", "fy"), "翻译文本或图片", "friend", 0, 0),
    HelpHandle("HelpHandle", Arrays.asList("帮助", "help", "?", "？"), "获取帮助", "friend", 0, 0),
    PatternStringHandle("PatternStringHandle", Arrays.asList("正则", "zz"), "正则匹配", "friend", 0, 0),
    RenameHandle("RenameHandle", Collections.emptyList(), "改名。", "group", 1, 2),
    NoBakaHandle("NoBakaHandle", Collections.emptyList(), "不笨。", "group", 1, 1),
    RepeatHandle("RepeatHandle", Collections.emptyList(), "复读。", "group", 1, 0),
    VoiceHandle("VoiceHandle", Arrays.asList("说", "s"), "文本转语音（日语）", "group", 0, 0),
    PixivHandle("PixivHandle", Arrays.asList("色图", "st"), "色图！（其实一点都不色（只有我才能要色图哈哈哈哈", "group", 0, 1),
    RecallHandle("RecallHandle", Arrays.asList("撤回", "ch"), "撤回色图！（想看误发的色图？想得美", "friend", 0, 0),
    ;

    private final String name;
    private final List<String> keyword;
    private final String description;
    private final String sendType;
    private final Integer type;
    private final Integer sort;

    MessageHandleEnum(String name, List<String> keyword, String description, String sendType, Integer type, Integer sort) {
        this.name = name;
        this.keyword = keyword;
        this.description = description;
        this.sendType = sendType;
        this.type = type;
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public List<String> getKeyword() {
        return keyword;
    }

    public String getDescription() {
        return description;
    }

    public String getSendType() {
        return sendType;
    }

    public Integer getType() {
        return type;
    }

    public Integer getSort() {
        return sort;
    }
}
