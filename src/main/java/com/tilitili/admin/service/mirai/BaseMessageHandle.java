package com.tilitili.admin.service.mirai;

import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.common.entity.mirai.MiraiMessage;

import java.util.List;

public interface BaseMessageHandle {
    MessageHandleEnum getType();
    MiraiMessage handleMessage(MiraiRequest request) throws Exception;
}
