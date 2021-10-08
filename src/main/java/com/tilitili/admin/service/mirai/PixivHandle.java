package com.tilitili.admin.service.mirai;

import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.utils.StringUtil;
import com.tilitili.common.emnus.RedisKeyEnum;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.Sender;
import com.tilitili.common.entity.pixiv.SearchIllustMangaData;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.manager.PixivManager;
import com.tilitili.common.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class PixivHandle implements BaseMessageHandle {
    public static final String messageIdKey = "pixiv.messageId";

    private final AtomicBoolean lockFlag = new AtomicBoolean(false);

    private final RedisCache redisCache;
    private final MiraiManager miraiManager;
    private final PixivManager pixivManager;

    @Autowired
    public PixivHandle(RedisCache redisCache, MiraiManager miraiManager, PixivManager pixivManager) {
        this.redisCache = redisCache;
        this.miraiManager = miraiManager;
        this.pixivManager = pixivManager;
    }

    @Override
    public MessageHandleEnum getType() {
        return MessageHandleEnum.PixivHandle;
    }

    @Override
    public MiraiMessage handleMessage(MiraiRequest request) throws Exception {
        if (!lockFlag.compareAndSet(false, true)) {
            return null;
        }
        Sender sender = request.getMessage().getSender();
        Sender sendGroup = sender.getGroup();
        String tag = request.getParamOrDefault("tag", "チルノ");
        MiraiMessage result = new MiraiMessage();
        try {
            Long offset = redisCache.increment(RedisKeyEnum.SPIDER_PIXIV_OFFSET.getKey(), tag);
            Long pageNo = offset / 60 + 1;
            int index = Math.toIntExact(offset % 60 - 1);

            List<SearchIllustMangaData> dataList = pixivManager.search(tag, pageNo);
            if (dataList.size() > index) {
                String imageUrl = dataList.get(index).getUrl();
                String id = dataList.get(index).getId();
                String subUrl = StringUtil.matcherGroupOne("(/img/..../../../../../../)", imageUrl);
                String type = StringUtil.matcherGroupOne("((?:png|jpg))", imageUrl);
                if (subUrl == null || type == null) {
                    miraiManager.sendFriendMessage("Plain", "异常 id = " + id + " url=" + imageUrl);
                }
                String bigImageUrl = String.format("https://i.pximg.net/img-original%s%s_p0.%s", subUrl, id, type);
                BufferedImage image = pixivManager.downloadImage(bigImageUrl);
                File tempFile = File.createTempFile("pixivImage", "."+type);
                System.out.println(tempFile.getPath());
                ImageIO.write(image, type, tempFile);
                String imageId = miraiManager.uploadImage(tempFile);
                tempFile.delete();
                Integer messageId = miraiManager.sendMessage(new MiraiMessage().setMessageType("Image").setSendType("group").setImageId(imageId).setGroup(sendGroup.getId()));
                redisCache.setValue(messageIdKey, String.valueOf(messageId));
            }
            lockFlag.set(false);
            return result.setMessage("").setMessageType("Plain");
        } catch (Exception e) {
            log.error("找色图失败",e);
            redisCache.increment(RedisKeyEnum.SPIDER_PIXIV_OFFSET.getKey(), tag, -1);
            lockFlag.set(false);
            return null;
        }
    }
}
