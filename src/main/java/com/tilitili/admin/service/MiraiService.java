package com.tilitili.admin.service;

import com.google.common.collect.ImmutableMap;
import com.tilitili.common.emnus.TaskReason;
import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.Subscription;
import com.tilitili.common.entity.mirai.MessageChain;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.entity.mirai.Sender;
import com.tilitili.common.entity.view.SimpleTaskView;
import com.tilitili.common.manager.BaiduManager;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.mapper.RecommendMapper;
import com.tilitili.common.mapper.RecommendVideoMapper;
import com.tilitili.common.mapper.SubscriptionMapper;
import com.tilitili.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Slf4j
@Service
public class MiraiService {

    private final RecommendMapper recommendMapper;
    private final RecommendVideoMapper recommendVideoMapper;
    private final SubscriptionMapper subscriptionMapper;
    private final TaskManager taskManager;
    private final BaiduManager baiduManager;

    @Autowired
    public MiraiService(RecommendMapper recommendMapper, RecommendVideoMapper recommendVideoMapper, SubscriptionMapper subscriptionMapper, TaskManager taskManager, BaiduManager baiduManager) {
        this.recommendMapper = recommendMapper;
        this.recommendVideoMapper = recommendVideoMapper;
        this.subscriptionMapper = subscriptionMapper;
        this.taskManager = taskManager;
        this.baiduManager = baiduManager;
    }

    public String handleMessage(MiraiMessageView message, Map<String, String> miraiSession) {
        try {
            List<MessageChain> messageChain = message.getMessageChain();
            String text = messageChain.stream().filter(StreamUtil.isEqual(MessageChain::getType, "Plain")).map(MessageChain::getText).collect(Collectors.joining("\n"));
            String url = messageChain.stream().filter(StreamUtil.isEqual(MessageChain::getType, "Image")).map(MessageChain::getUrl).findFirst().orElse("");
            String[] textList = text.split("\n");

            String title;
            String body;
            if (miraiSession.containsKey("模式")) {
                title = miraiSession.get("模式");
                body = text;
                if (Objects.equals(textList[0], "退出")) {
                    miraiSession.remove("模式");
                    return "停止"+title;
                }
            } else {
                title = textList[0];
                body = Stream.of(textList).skip(1).collect(Collectors.joining("\n"));
                if (textList[0].contains("模式")) {
                    String mod = textList[0].replaceAll("模式", "");
                    miraiSession.put("模式", mod);
                    return "开始"+mod;
                }
            }

            String[] bodyList = body.split("\n");
            Map<String, String> map = new HashMap<>();
            for (String line : bodyList) {
                if (!line.contains("=")) {
                    continue;
                }
                String key = line.split("=")[0];
                String value = line.split("=")[1];
                map.put(key.trim(), value.trim());
            }
            if (isNotBlank(body)) {
                map.put("body", body);
            }
            if (isNotBlank(url)) {
                map.put("url", url);
            }
            switch (title) {
                case "zaima" : return "buzai, cmn";
                case "推荐" : case "recommon" :return addRecommendFromMessage(message, map);
                case "关注" : case "follow" :return addSubscriptionFromMessage(message, map);
                case "找图" : case "find" :return findImageFromMessage(message, map);
                case "翻译": case "translate" :return translateFromMessage(message, map);
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

    private String translateFromMessage(MiraiMessageView message, Map<String, String> map) {
        String body = map.getOrDefault("body", "");
        String url = map.getOrDefault("url", "");
        Asserts.notBlank(body + url, "格式错啦(内容)");
        String result;
        if (isNotBlank(body)) {
            result = baiduManager.translate(body);
        } else {
            result = baiduManager.translateImage(url);
        }
        if (isBlank(result)) {
            return "无法翻译";
        }
        return result;
    }//http://c2cpicdw.qpic.cn/offpic_new/545459363//545459363-1286451716-A171CF4B966BAC4B3CEDE4A83DD0AE53/0?term=2

    private String findImageFromMessage(MiraiMessageView message, Map<String, String> map) {
        String url = map.get("url");
        Asserts.notBlank(url, "格式错啦(图片)");
        String html = HttpClientUtil.httpPost("https://saucenao.com/search.php?url="+url, ImmutableMap.of());
        Asserts.notBlank(html, "没要到图😇\n"+url);
        Document document = Jsoup.parse(html);
        Elements resultList = document.select(".result:not(.hidden):not(#result-hidden-notification)");
        Asserts.isFalse(resultList.isEmpty(), "没找到🤕\n"+url);
        Element result = resultList.get(0);
        String rate = result.select(".resultsimilarityinfo").text();
        Elements linkList = result.select(".resultcontentcolumn a.linkify");
        String link = linkList.get(0).attr("href");
        return String.format("找到啦😊！相似度%s\n%s", rate, link);
    }

    public String addSubscriptionFromMessage(MiraiMessageView message, Map<String, String> map) {
        String uid = map.get("uid");
        Sender sender = message.getSender();
        Long qq = sender.getId();
        Long group = Optional.ofNullable(sender.getGroup()).map(Sender::getId).orElse(null);

        Asserts.notBlank(uid, "格式错啦(uid)");

        int oldCount = subscriptionMapper.countSubscriptionByCondition(new Subscription().setType(1).setValue(uid).setSendQq(qq));
        Asserts.isTrue(oldCount == 0, "已经关注了哦。");

        Subscription add = new Subscription().setValue(uid).setType(1).setSendGroup(group).setSendQq(qq).setSendType("friend");
        subscriptionMapper.insertSubscription(add);

        SimpleTaskView simpleTaskView = new SimpleTaskView().setValue(uid).setReason(TaskReason.SUPPLEMENT_VIDEO_OWNER.value);
        taskManager.simpleSpiderVideo(simpleTaskView);
        return "关注成功！";
    }

    public String addRecommendFromMessage(MiraiMessageView message, Map<String, String> map) {
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
