package com.tilitili.admin.service.mirai;

import com.google.gson.Gson;
import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.service.MiraiSessionService;
import com.tilitili.common.emnus.RedisKeyEnum;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.Sender;
import com.tilitili.common.entity.vilipix.IllustResponse;
import com.tilitili.common.entity.vilipix.IllustRows;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.HttpClientUtil;
import com.tilitili.common.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PixivHandle implements BaseMessageHandle {
    @Value("${mirai.master-qq}")
    private Long MASTER_QQ;

    public static final String typeKey = "pixiv.type";
    public static final String messageIdKey = "pixiv.messageId";

    private final RedisCache redisCache;
    private final MiraiManager miraiManager;

    @Autowired
    public PixivHandle(RedisCache redisCache, MiraiManager miraiManager) {
        this.redisCache = redisCache;
        this.miraiManager = miraiManager;
    }

    @Override
    public MessageHandleEnum getType() {
        return MessageHandleEnum.PixivHandle;
    }

    @Override
    public MiraiMessage handleMessage(MiraiRequest request) throws Exception {
        Sender sender = request.getMessage().getSender();
        Sender sendGroup = sender.getGroup();
        String tag = request.getParamOrDefault("tag", "ロリ");
        MiraiMessage result = new MiraiMessage();

        if (sender.getId().equals(MASTER_QQ)) {
            Long offset = redisCache.increment(RedisKeyEnum.SPIDER_PIXIV_OFFSET.getKey(), tag);
            String url = "https://vilipix.com/api/illust/tag/" + tag + "?limit=1&offset=" + offset;
            String jsonStr = HttpClientUtil.httpGet(url);
            Asserts.notBlank(jsonStr, "没要到图😇\n"+url);
            IllustResponse illustResponse = new Gson().fromJson(jsonStr, IllustResponse.class);
            List<IllustRows> rows = illustResponse.getRows();
            Asserts.isFalse(rows.isEmpty(), "没了🤕\n"+url);
            IllustRows image = rows.get(0);
            String imageUrl = image.getRegular_url();
            Asserts.notBlank(imageUrl, "没了🤕\n"+url);
            Integer messageId = miraiManager.sendGroupMessage("Image", imageUrl, sendGroup.getId());

            redisCache.setValue(messageIdKey, String.valueOf(messageId));

            return result.setMessage("").setMessageType("Plain");
        }

        return null;
    }
}
