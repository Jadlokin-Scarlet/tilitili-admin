package com.tilitili.admin.service;

import com.tilitili.admin.utils.StringUtil;
import com.tilitili.common.entity.RecommendTalk;
import com.tilitili.common.entity.query.RecommendTalkQuery;
import com.tilitili.common.exception.AssertException;
import com.tilitili.common.manager.RecommendTalkManager;
import com.tilitili.common.mapper.rank.RecommendTalkMapper;
import com.tilitili.common.mapper.rank.RecommendVideoMapper;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.StreamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendTalkService {
    private final RecommendTalkMapper recommendTalkMapper;
    private final RecommendTalkManager recommendTalkManager;
    private final RecommendVideoMapper recommendVideoMapper;

    @Autowired
    public RecommendTalkService(RecommendTalkMapper recommendTalkMapper, RecommendTalkManager recommendTalkManager, RecommendVideoMapper recommendVideoMapper) {
        this.recommendTalkMapper = recommendTalkMapper;
        this.recommendTalkManager = recommendTalkManager;
        this.recommendVideoMapper = recommendVideoMapper;
    }

    public String getTotalNewRecommendTalk(Integer type) {
        Integer issueId = recommendVideoMapper.getNew().getId();
        RecommendTalkQuery query = new RecommendTalkQuery().setIssueId(issueId).setType(type).setStatus(0).setSorter("id").setSorted("asc");
        List<RecommendTalk> recommendTalkList = recommendTalkMapper.getRecommendTalkByCondition(query);
        return recommendTalkList.stream().map(this::mapRecommendTalkToString).collect(Collectors.joining("\n"));
    }

    private String mapRecommendTalkToString(RecommendTalk recommendTalk) {
        return String.format("%s：%s（%s）", recommendTalk.getSpeaker(), recommendTalk.getText(), recommendTalk.getExpression());
    }

    @Transactional(transactionManager = "rankTransactionManager")
    public void batchUpdate(String op, Integer type) {
        String area = type == 1? "OP": "ED";
        Integer issueId = recommendVideoMapper.getNew().getId();
        String[] talkList = op.split("\n");
        List<String> speakerList = Arrays.asList("灵梦", "早苗", "灵梦&早苗");
        List<String> reimuExpression = Arrays.asList("默认", "龇牙", "互动", "微笑", "気持", "思考向上", "GJ", "汗颜", "思考");
        List<String> sanaeExpression = Arrays.asList("默认", "开心", "猫耳", "互动", "汗颜", "好奇", "汇报");

        List<RecommendTalk> oldRecommendTalkList = recommendTalkMapper.getRecommendTalkByCondition(new RecommendTalkQuery().setIssueId(issueId).setType(type).setStatus(0));
        Map<Integer, String> voiceMap = oldRecommendTalkList.stream().filter(StreamUtil.isNotNull(RecommendTalk::getVoiceUrl)).collect(Collectors.toMap(RecommendTalk::getIndex, RecommendTalk::getVoiceUrl));

        List<RecommendTalk> recommendTalkList = new ArrayList<>();
        for (Integer index = 1; index <= talkList.length; index++) {
            String talk = talkList[index - 1];
            String speaker = StringUtil.matcherGroupOne("^(.+?)：", talk);
            String text = StringUtil.matcherGroupOne("：(.+)（", talk);
            String expression = StringUtil.matcherGroupOne("([^（]+)）$", talk);
            Asserts.notNull(speaker, "%s第%s行，无法识别主持人", area, index);
            Asserts.notNull(text, "%s第%s行，无法识别台本", area, index);
            Asserts.notNull(expression, "%s第%s行，无法识别表情", area, index);

            Asserts.isTrue(speakerList.contains(speaker), "%s第%s行，没有角色%s", area, index, speaker);
            switch (speaker) {
                case "灵梦": Asserts.isTrue(reimuExpression.contains(expression), "%s第%s行，%s没有表情%s", area, index, speaker, expression);break;
                case "早苗": Asserts.isTrue(sanaeExpression.contains(expression), "%s第%s行，%s没有表情%s", area, index, speaker, expression);break;
                case "灵梦&早苗": Asserts.isTrue(reimuExpression.contains(expression) && sanaeExpression.contains(expression), "%s第%s行，灵梦或早苗没有表情%s", area, index, speaker, expression);break;
                default: throw new AssertException("不对劲");
            }

            RecommendTalk recommendTalk = new RecommendTalk().setSpeaker(speaker).setText(text).setExpression(expression).setType(type).setIssueId(issueId).setIndex(index).setVoiceUrl(voiceMap.get(index));
            recommendTalkList.add(recommendTalk);
        }
        recommendTalkManager.batchDeleteAndAdd(issueId, type, recommendTalkList);
    }

}
