package com.tilitili.admin.service;

import com.tilitili.common.emnus.TaskReason;
import com.tilitili.common.entity.Owner;
import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.Subscription;
import com.tilitili.common.entity.mirai.MessageChain;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.Sender;
import com.tilitili.common.entity.view.SimpleTaskView;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.mapper.OwnerMapper;
import com.tilitili.common.mapper.RecommendMapper;
import com.tilitili.common.mapper.RecommendVideoMapper;
import com.tilitili.common.mapper.SubscriptionMapper;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.BilibiliUtil;
import com.tilitili.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class MiraiService {

    private final RecommendMapper recommendMapper;
    private final RecommendVideoMapper recommendVideoMapper;
    private final SubscriptionMapper subscriptionMapper;
    private final OwnerMapper ownerMapper;
    private final TaskManager taskManager;

    @Autowired
    public MiraiService(RecommendMapper recommendMapper, RecommendVideoMapper recommendVideoMapper, SubscriptionMapper subscriptionMapper, OwnerMapper ownerMapper, TaskManager taskManager) {
        this.recommendMapper = recommendMapper;
        this.recommendVideoMapper = recommendVideoMapper;
        this.subscriptionMapper = subscriptionMapper;
        this.ownerMapper = ownerMapper;
        this.taskManager = taskManager;
    }

    public String handleMessage(MiraiMessage message) {
        try {
            List<MessageChain> messageChain = message.getMessageChain();
            if (messageChain.size() != 2) {
                return "";
            }
            MessageChain messageChainItem = messageChain.get(1);
            if (! Objects.equals(messageChainItem.getType(), "Plain")) {
                return "";
            }
            String text = messageChainItem.getText();
            String[] lineList = text.split("\n");
            String title = lineList[0];
            Map<String, String> map = new HashMap<>();
            for (int index = 1; index < lineList.length; index++) {
                String line = lineList[index];
                if (!line.contains("=")) {
                    continue;
                }
                String key = line.split("=")[0];
                String value = line.split("=")[1];
                map.put(key.trim(), value.trim());
            }
            switch (title) {
                case "添加推荐" : return addRecommendFromMessage(message, map);
                case "关注主播" : return addSubscriptionFromMessage(message, map);
                default: return "?";
            }

        } catch (IllegalStateException e) {
            log.error(e.getMessage());
            return e.getMessage();
        }catch (Exception e) {
            log.error("处理消息回调失败",e);
            return "¿";
        }
    }

    public String addSubscriptionFromMessage(MiraiMessage message, Map<String, String> map) {
        String uid = map.get("uid");
        Sender sender = message.getSender();
        Long qq = sender.getId();

        Asserts.notBlank(uid, "格式错啦(uid)");

        int oldCount = subscriptionMapper.countSubscriptionByCondition(new Subscription().setType(1).setValue(uid).setSendGroup(qq));
        Asserts.isTrue(oldCount == 0, "已经关注了哦。");

        Subscription add = new Subscription().setValue(uid).setType(1).setSendGroup(qq).setSendType("friend");
        subscriptionMapper.insertSubscription(add);

        SimpleTaskView simpleTaskView = new SimpleTaskView().setValue(uid).setReason(TaskReason.SUPPLEMENT_VIDEO_OWNER.value);
        taskManager.simpleSpiderVideo(simpleTaskView);
        return "关注成功！";
    }

    public String addRecommendFromMessage(MiraiMessage message, Map<String, String> map) {
        String avStr = map.get("视频号");
        String operator = map.get("推荐人");
        String text = map.get("推荐语");
        int startTime = Integer.parseInt(map.getOrDefault("开始时间", "0"));
        int endTime = Integer.parseInt(map.getOrDefault("结束时间", String.valueOf(startTime + 30)));

        Asserts.notNull(avStr, "格式错啦(视频号)");
        Asserts.notNull(operator, "格式错啦(推荐人)");
        Asserts.notBlank(text, "格式错啦(推荐语)");
        Asserts.isTrue(startTime < endTime, "时间线错乱啦");

        long av;
        if (StringUtils.isNumber(avStr)) {
            av = Long.parseLong(avStr);
        } else {
            av = BilibiliUtil.converseAvToBv(avStr);
        }
        Recommend oldRecommend = recommendMapper.getByAv(av);
        Asserts.checkNull(oldRecommend, "这个视频已经有啦");

        RecommendVideo recommendVideo = recommendVideoMapper.getNew();


        Recommend recommend = new Recommend();
        recommend.setAv(av);
        recommend.setStatus(1);
        recommend.setText(text);
        recommend.setOperator(operator);
        recommend.setIssueId(recommendVideo.getId());
        recommend.setStartTime(startTime);
        recommend.setEndTime(endTime);
        recommendMapper.insert(recommend);
        return "收到！";
    }

}
