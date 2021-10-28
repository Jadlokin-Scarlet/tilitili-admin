package com.tilitili.admin.service.mirai;

import com.tilitili.admin.emnus.MessageHandleEnum;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.common.emnus.RedisKeyEnum;
import com.tilitili.common.emnus.TaskReason;
import com.tilitili.common.entity.PixivImage;
import com.tilitili.common.entity.lolicon.SetuData;
import com.tilitili.common.entity.mirai.MessageChain;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.Sender;
import com.tilitili.common.entity.pixivmoe.GetIllust;
import com.tilitili.common.entity.pixivmoe.GetImageUrls;
import com.tilitili.common.entity.pixivmoe.GetMetaPages;
import com.tilitili.common.entity.pixivmoe.SearchIllust;
import com.tilitili.common.entity.view.SimpleTaskView;
import com.tilitili.common.exception.AssertException;
import com.tilitili.common.manager.LoliconManager;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.manager.PixivMoeManager;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.mapper.PixivImageMapper;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.apache.http.util.TextUtils.isBlank;

@Slf4j
@Component
public class PixivHandle implements BaseMessageHandle {
    public static final String messageIdKey = "pixiv.messageId";

    private final AtomicBoolean lockFlag = new AtomicBoolean(false);

    private final RedisCache redisCache;
    private final MiraiManager miraiManager;
    private final PixivMoeManager pixivMoeManager;
    private final PixivImageMapper pixivImageMapper;
    private final LoliconManager loliconManager;
    private final TaskManager taskManager;

    @Autowired
    public PixivHandle(RedisCache redisCache, MiraiManager miraiManager, PixivMoeManager pixivMoeManager, PixivImageMapper pixivImageMapper, LoliconManager loliconManager, TaskManager taskManager) {
        this.redisCache = redisCache;
        this.miraiManager = miraiManager;
        this.pixivMoeManager = pixivMoeManager;
        this.pixivImageMapper = pixivImageMapper;
        this.loliconManager = loliconManager;
        this.taskManager = taskManager;
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
            String source = request.getParamOrDefault("source", "pixiv.moe");
            String num = request.getParamOrDefault("num", "1");
            Long sendMessageId = request.getMessageId();
            MiraiMessage result = new MiraiMessage();

            Integer messageId;
            switch (source) {
                case "pixiv.moe": messageId = sendPixivMoeImage(sendGroup, searchKey, source); break;
                case "pixiv": messageId = sendPixivImage(sendGroup, searchKey, source, sendMessageId); break;
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

    private Integer sendPixivImage(Sender sendGroup, String searchKey, String source, Long sendMessageId) {
        PixivImage noUsedImage = pixivImageMapper.getNoUsedImage(searchKey, source);
        if (noUsedImage == null) {
            Long pageNo = redisCache.increment(RedisKeyEnum.SPIDER_PIXIV_PAGENO.getKey(), searchKey);
            taskManager.simpleSpiderVideo(new SimpleTaskView().setReason(TaskReason.SPIDER_PIXIV.value).setValueList(Arrays.asList(searchKey, String.valueOf(pageNo), String.valueOf(sendMessageId))));
            miraiManager.sendMessage(new MiraiMessage().setMessageType("ImageText").setSendType("group").setMessage(String.format("[%s]还没有，我找找。", searchKey)).setUrl("http://gchat.qpic.cn/gchatpic_new/545459363/902813629-2385307943-99D0157B56ABB1C58B0BB2AC1680DB3E/0?term=2").setGroup(sendGroup.getId()));
            return null;
        }

        String pid = noUsedImage.getPid();
        String[] urlList = noUsedImage.getUrlList().split(",");

        ArrayList<MessageChain> messageChainList = new ArrayList<>();
        messageChainList.add(new MessageChain().setType("Plain").setText("https://pixiv.moe/illust/"+pid+"\n"));
        messageChainList.add(new MessageChain().setType("Image").setUrl(urlList[0]));
        if (urlList.length > 1) {
            messageChainList.add(new MessageChain().setType("Plain").setText("\n(更多略)"));
        }
        pixivImageMapper.updatePixivImage(new PixivImage().setId(noUsedImage.getId()).setStatus(1));
        Integer messageId = miraiManager.sendMessage(new MiraiMessage().setMessageType("List").setSendType("group").setMessageChainList(messageChainList).setGroup(sendGroup.getId()));
        pixivImageMapper.updatePixivImage(new PixivImage().setId(noUsedImage.getId()).setStatus(1).setMessageId(messageId));
        return messageId;
    }

    private Integer sendLoliconImage(Sender sendGroup, String searchKey, String source, String num) throws InterruptedException {
        List<SetuData> dataList = loliconManager.getAImage(searchKey, num);
        List<MessageChain> messageChainList = new ArrayList<>();
        Integer messageId;
        for (SetuData data : dataList) {
            String pid = String.valueOf(data.getPid());
            String imageUrl = data.getUrls().getOriginal();
            boolean isSese = data.getTags().contains("R-18") || data.getR18();
            if (isSese) {
                messageChainList.add(new MessageChain().setType("Plain").setText(imageUrl + "\n"));
            } else {
                messageChainList.add(new MessageChain().setType("Plain").setText(pid + "\n"));
                messageChainList.add(new MessageChain().setType("Image").setUrl(imageUrl));
                messageChainList.add(new MessageChain().setType("Plain").setText("\n"));
            }
        }
        messageId = miraiManager.sendMessage(new MiraiMessage().setMessageType("List").setSendType("group").setMessageChainList(messageChainList).setGroup(sendGroup.getId()));

        for (SetuData data : dataList) {
            String pid = String.valueOf(data.getPid());
            String imageUrl = data.getUrls().getOriginal();

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

    private Integer sendPixivMoeImage(Sender sendGroup, String searchKey, String source) throws InterruptedException {
        PixivImage noUsedImage = pixivImageMapper.getNoUsedImage(searchKey, source);
        if (noUsedImage == null) {
            List<SearchIllust> dataList = pixivMoeManager.search(searchKey, 1L);
            Asserts.isFalse(dataList.isEmpty(), "搜不到tag");
            List<SearchIllust> filterDataList = dataList.stream().filter(data -> pixivImageMapper.listPixivImageByCondition(new PixivImage().setPid(data.getId())).isEmpty()).collect(Collectors.toList());

            if (filterDataList.isEmpty()) {
                Long pageNo = redisCache.increment(RedisKeyEnum.SPIDER_PIXIV_PAGENO.getKey(), searchKey);
                filterDataList = pixivMoeManager.search(searchKey, pageNo);
                Asserts.isFalse(filterDataList.isEmpty(), "搜不到tag");
            }

            for (SearchIllust data : filterDataList) {
                String pid = data.getId();

                PixivImage pixivImage = new PixivImage();
                pixivImage.setPid(pid);
                pixivImage.setTitle(data.getTitle());
                pixivImage.setPageCount(data.getPage_count());
                pixivImage.setSmallUrl(data.getImage_urls().getOriginal());
                pixivImage.setUserName(data.getUser().getName());
                pixivImage.setUserId(data.getUser().getId());
                pixivImage.setSearchKey(searchKey);
                pixivImage.setSource(source);

                List<PixivImage> oldDataList = pixivImageMapper.listPixivImageByCondition(new PixivImage().setPid(pid).setSource(source));
                if (oldDataList.isEmpty()) {
                    pixivImageMapper.insertPixivImage(pixivImage);
                }
            }
            noUsedImage = pixivImageMapper.getNoUsedImage(searchKey, source);
        }

        String url = noUsedImage.getSmallUrl();
        String pid = noUsedImage.getPid();

        Integer messageId = miraiManager.sendMessage(new MiraiMessage().setMessageType("ImageText").setSendType("group").setUrl(url.replace("https://", "https://api.pixiv.moe/image/")).setMessage("https://pixiv.moe/illust/"+pid+"\n").setGroup(sendGroup.getId()));
        pixivImageMapper.updatePixivImage(new PixivImage().setId(noUsedImage.getId()).setStatus(1).setMessageId(messageId));
        return messageId;
    }
}
