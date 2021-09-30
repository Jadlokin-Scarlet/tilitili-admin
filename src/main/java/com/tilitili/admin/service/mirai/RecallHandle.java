package com.tilitili.admin.service.mirai;

import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.service.MiraiSessionService;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.Sender;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.jsoup.helper.StringUtil.isBlank;

@Component
public class RecallHandle implements BaseMessageHandle {
    @Value("${mirai.master-qq}")
    private Long MASTER_QQ;

    private final MiraiManager miraiManager;
    private final RedisCache redisCache;

    @Autowired
    public RecallHandle(MiraiManager miraiManager, RedisCache redisCache) {
        this.miraiManager = miraiManager;
        this.redisCache = redisCache;
    }

    @Override
    public MessageHandleEnum getType() {
        return MessageHandleEnum.RecallHandle;
    }

    @Override
    public MiraiMessage handleMessage(MiraiRequest request) throws Exception {
        Sender sender = request.getMessage().getSender();
        MiraiSessionService.MiraiSession session = request.getSession();
        MiraiMessage result = new MiraiMessage();

        if (sender.getId().equals(MASTER_QQ)) {
            String messageIdStr = (String) redisCache.getValue(PixivHandle.messageIdKey);
            if (! isBlank(messageIdStr)) {
                long messageId = Long.parseLong(messageIdStr);
                miraiManager.recallMessage(messageId);
                return result;
            }
        }
        return null;
    }
}
