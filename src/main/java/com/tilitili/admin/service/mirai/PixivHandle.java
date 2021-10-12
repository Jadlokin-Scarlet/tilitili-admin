package com.tilitili.admin.service.mirai;

import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.utils.StringUtil;
import com.tilitili.common.emnus.RedisKeyEnum;
import com.tilitili.common.entity.PixivImage;
import com.tilitili.common.entity.lolicon.SetuData;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.Sender;
import com.tilitili.common.entity.pixiv.SearchIllustMangaData;
import com.tilitili.common.exception.AssertException;
import com.tilitili.common.manager.LoliconManager;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.manager.PixivManager;
import com.tilitili.common.mapper.PixivImageMapper;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private final LoliconManager loliconManager;

    @Autowired
    public PixivHandle(RedisCache redisCache, MiraiManager miraiManager, PixivManager pixivManager, PixivImageMapper pixivImageMapper, LoliconManager loliconManager) {
        this.redisCache = redisCache;
        this.miraiManager = miraiManager;
        this.pixivManager = pixivManager;
        this.pixivImageMapper = pixivImageMapper;
        this.loliconManager = loliconManager;
    }

    @Override
    public MessageHandleEnum getType() {
        return MessageHandleEnum.PixivHandle;
    }

    @Override
    public MiraiMessage handleMessage(MiraiRequest request) throws Exception {
        if (!lockFlag.compareAndSet(false, true)) {
            log.warn("色图锁了，跳过");
            return null;
        }
        try {
            Sender sender = request.getMessage().getSender();
            Sender sendGroup = sender.getGroup();
            String searchKey = request.getTitleValueOrDefault(request.getParamOrDefault("tag", "チルノ"));
            String source = request.getParamOrDefault("source", "lolicon");
            String num = request.getParamOrDefault("num", "1");
            MiraiMessage result = new MiraiMessage();

            Integer messageId;
            switch (source) {
                case "pixiv": messageId = sendPixivImage(sendGroup, searchKey, source); break;
                case "lolicon": messageId = sendLoliconImage(sendGroup, searchKey, source, num); break;
                default: throw new AssertException("不支持的平台");
            }
            Asserts.notNull(messageId, "发送失败");
            redisCache.setValue(messageIdKey, String.valueOf(messageId));
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

    private Integer sendLoliconImage(Sender sendGroup, String searchKey, String source, String num) throws InterruptedException, IOException {
        SetuData data = loliconManager.getAImage(searchKey, num);
        String pid = String.valueOf(data.getPid());
        String imageUrl = data.getUrls().getOriginal();
        boolean isSese = data.getTags().contains("R-18") || data.getR18();
        Integer messageId;
        if (isSese) {
            messageId = miraiManager.sendMessage(new MiraiMessage().setMessageType("Plain").setSendType("group").setMessage(imageUrl).setGroup(sendGroup.getId()));
        } else {
            messageId = miraiManager.sendMessage(new MiraiMessage().setMessageType("ImageText").setSendType("group").setUrl(imageUrl).setMessage(pid).setGroup(sendGroup.getId()));
        }

        List<PixivImage> oldDataList = pixivImageMapper.listPixivImageByCondition(new PixivImage().setPid(pid).setSource("lolicon"));
        if (oldDataList.isEmpty()) {
            PixivImage pixivImage = new PixivImage();
            pixivImage.setPid(pid);
            pixivImage.setTitle(data.getTitle());
            pixivImage.setPageCount(1);
            pixivImage.setSmallUrl(imageUrl);
            pixivImage.setUserId(String.valueOf(data.getUid()));
            pixivImage.setUrlList(imageUrl);
            pixivImage.setSearchKey(searchKey);
            pixivImage.setSource("lolicon");
            pixivImage.setStatus(1);
            pixivImage.setMessageId(messageId);
            pixivImageMapper.insertPixivImage(pixivImage);
        }
        return messageId;
    }

    private Integer sendPixivImage(Sender sendGroup, String searchKey, String source) throws InterruptedException, IOException {

        PixivImage noUsedImage = pixivImageMapper.getNoUsedImage(searchKey, source);
        if (noUsedImage == null) {
            supplePixivImage(searchKey);
            noUsedImage = pixivImageMapper.getNoUsedImage(searchKey, source);
        }

        List<String> bigImageList;
        if (noUsedImage.getUrlList() == null) {
            bigImageList = pixivManager.getBigImageList(noUsedImage.getPid());
            Asserts.isFalse(bigImageList.isEmpty(), "读不到大图");
            pixivImageMapper.updatePixivImage(new PixivImage().setId(noUsedImage.getId()).setUrlList(String.join(",", bigImageList)));
        } else {
            bigImageList = Arrays.stream(noUsedImage.getUrlList().split(",")).collect(Collectors.toList());
        }

        List<String> imageIdList = new ArrayList<>();
        for (String imageUrl : bigImageList) {
            String type = StringUtil.matcherGroupOne("((?:png|jpg))", imageUrl);
            BufferedImage image = pixivManager.downloadImage(imageUrl);
            File tempFile = File.createTempFile("pixivImage", "." + type);
            ImageIO.write(image, type, tempFile);
            String imageId = miraiManager.uploadImage(tempFile);
            tempFile.delete();
            imageIdList.add(imageId);
        }
        Integer messageId = miraiManager.sendMessage(new MiraiMessage().setMessageType("ImageList").setSendType("group").setImageIdList(imageIdList).setGroup(sendGroup.getId()));
        pixivImageMapper.updatePixivImage(new PixivImage().setId(noUsedImage.getId()).setStatus(1));
        return messageId;
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
            pixivImage.setSource("pixiv");

            List<PixivImage> oldDataList = pixivImageMapper.listPixivImageByCondition(new PixivImage().setPid(pid).setSource("pixiv"));
            if (oldDataList.isEmpty()) {
                pixivImageMapper.insertPixivImage(pixivImage);
            }
        }

    }
}
