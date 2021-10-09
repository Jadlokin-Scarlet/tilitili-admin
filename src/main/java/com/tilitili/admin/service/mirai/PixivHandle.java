package com.tilitili.admin.service.mirai;

import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.utils.StringUtil;
import com.tilitili.common.emnus.RedisKeyEnum;
import com.tilitili.common.entity.PixivImage;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.Sender;
import com.tilitili.common.entity.pixiv.SearchIllustMangaData;
import com.tilitili.common.exception.AssertException;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.manager.PixivManager;
import com.tilitili.common.mapper.PixivImageMapper;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.RedisCache;
import com.tilitili.common.utils.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PixivHandle implements BaseMessageHandle {
    public static final String messageIdKey = "pixiv.messageId";

    private final AtomicBoolean lockFlag = new AtomicBoolean(false);

    private final RedisCache redisCache;
    private final MiraiManager miraiManager;
    private final PixivManager pixivManager;
    private final PixivImageMapper pixivImageMapper;

    @Autowired
    public PixivHandle(RedisCache redisCache, MiraiManager miraiManager, PixivManager pixivManager, PixivImageMapper pixivImageMapper) {
        this.redisCache = redisCache;
        this.miraiManager = miraiManager;
        this.pixivManager = pixivManager;
        this.pixivImageMapper = pixivImageMapper;
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
        String searchKey = "";
        try {
            Sender sender = request.getMessage().getSender();
            Sender sendGroup = sender.getGroup();
            searchKey = request.getParamOrDefault("tag", "チルノ");
            MiraiMessage result = new MiraiMessage();

            PixivImage noUsedImage = pixivImageMapper.getNoUsedImage(searchKey);
            if (noUsedImage == null) {
                supplePixivImage(searchKey);
                noUsedImage = pixivImageMapper.getNoUsedImage(searchKey);
            }

            List<String> bigImageList;
            if (noUsedImage.getUrlList() == null) {
                bigImageList = pixivManager.getBigImageList(noUsedImage.getPid());
                Asserts.isFalse(bigImageList.isEmpty(), "读不到大图");
                pixivImageMapper.updatePixivImage(new PixivImage().setId(noUsedImage.getId()).setUrlList(String.join(",", bigImageList)));
            } else {
                bigImageList = Arrays.stream(noUsedImage.getUrlList().split(",")).collect(Collectors.toList());
            }

            List<String> imageIdList = bigImageList.stream().map(StreamUtil.tryMap(imageUrl -> {
                String type = StringUtil.matcherGroupOne("((?:png|jpg))", imageUrl);
                BufferedImage image = pixivManager.downloadImage(imageUrl);
                File tempFile = File.createTempFile("pixivImage", "."+type);//
                ImageIO.write(image, type, tempFile);
                String imageId = miraiManager.uploadImage(tempFile);
                tempFile.delete();
                return imageId;
            })).collect(Collectors.toList());
            Integer messageId = miraiManager.sendMessage(new MiraiMessage().setMessageType("ImageList").setSendType("group").setImageIdList(imageIdList).setGroup(sendGroup.getId()));
            redisCache.setValue(messageIdKey, String.valueOf(messageId));
            pixivImageMapper.updatePixivImage(new PixivImage().setId(noUsedImage.getId()).setStatus(1));
            lockFlag.set(false);
            return result.setMessage("").setMessageType("Plain");
        } catch (AssertException e) {
            log.error("找色图失败",e);
            lockFlag.set(false);
            throw e;
        } catch (Exception e) {
            log.error("找色图失败",e);
            lockFlag.set(false);
            return null;
        }
    }

    private void supplePixivImage(String searchKey) {
        List<SearchIllustMangaData> dataList = pixivManager.search(searchKey, 1L);
        Asserts.isFalse(dataList.isEmpty(), "搜不到tag");
        List<SearchIllustMangaData> filterDataList = dataList.stream().filter(data -> pixivImageMapper.listPixivImageByCondition(new PixivImage().setPid(data.getId())).isEmpty()).collect(Collectors.toList());

        if (filterDataList.isEmpty()) {
            Long pageNo = redisCache.increment(RedisKeyEnum.SPIDER_PIXIV_PAGENO.getKey(), searchKey);
            filterDataList = pixivManager.search(searchKey, pageNo);
            Asserts.isFalse(filterDataList.isEmpty(), "搜不到tag");
        }

        for (SearchIllustMangaData data : filterDataList) {
            String pid = data.getId();

            PixivImage pixivImage = new PixivImage();
            pixivImage.setPid(pid);
            pixivImage.setTitle(data.getTitle());
            pixivImage.setExternalCreateDate(data.getCreateDate());
            pixivImage.setExternalUpdateDate(data.getUpdateDate());
            pixivImage.setIllustType(data.getIllustType());
            pixivImage.setPageCount(data.getPageCount());
            pixivImage.setSmallUrl(data.getUrl());
            pixivImage.setUserName(data.getUserName());
            pixivImage.setUserId(data.getUserId());
            pixivImage.setSearchKey(searchKey);

            List<PixivImage> oldDataList = pixivImageMapper.listPixivImageByCondition(new PixivImage().setPid(pid));
            if (oldDataList.isEmpty()) {
                pixivImageMapper.insertPixivImage(pixivImage);
            }
        }

    }
}
