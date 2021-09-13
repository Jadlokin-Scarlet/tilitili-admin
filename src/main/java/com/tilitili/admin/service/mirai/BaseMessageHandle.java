package com.tilitili.admin.service.mirai;

import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.common.entity.mirai.MiraiMessage;

import java.util.List;

public interface BaseMessageHandle {
    List<String> getKeyword();
    String getDescription();
    String getSendType();
    Integer getType();
    MiraiMessage handleMessage(MiraiRequest request) throws Exception;
}
