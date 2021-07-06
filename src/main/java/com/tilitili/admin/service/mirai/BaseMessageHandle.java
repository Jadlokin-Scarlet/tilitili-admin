package com.tilitili.admin.service.mirai;

import com.tilitili.common.entity.mirai.MiraiMessageView;

import java.util.List;
import java.util.Map;

public interface BaseMessageHandle {
    List<String> getKeyword();
    String handleMessage(MiraiMessageView message, Map<String, String> map);
}
