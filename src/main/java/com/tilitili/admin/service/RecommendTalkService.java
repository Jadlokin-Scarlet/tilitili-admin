package com.tilitili.admin.service;

import com.tilitili.admin.utils.StringUtil;
import com.tilitili.common.entity.RecommendTalk;
import com.tilitili.common.entity.query.RecommendTalkQuery;
import com.tilitili.common.manager.RecommendTalkManager;
import com.tilitili.common.mapper.RecommendTalkMapper;
import com.tilitili.common.mapper.RecommendVideoMapper;
import com.tilitili.common.utils.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
        RecommendTalkQuery query = new RecommendTalkQuery().setIssueId(issueId).setType(type).setStatus(0).setSorter("id", "asc");
        List<RecommendTalk> recommendTalkList = recommendTalkMapper.list(query);
        return recommendTalkList.stream().map(this::mapRecommendTalkToString).collect(Collectors.joining("\n"));
    }

    private String mapRecommendTalkToString(RecommendTalk recommendTalk) {
        return String.format("%s：%s（%s）", recommendTalk.getSpeaker(), recommendTalk.getText(), recommendTalk.getExpression());
    }

    @Transactional
    public void batchUpdate(String op, Integer type) {
        String area = type == 1? "OP": "ED";
        Integer issueId = recommendVideoMapper.getNew().getId();
        String[] talkList = op.split("\n");
        List<String> speakerList = Arrays.asList("灵梦", "早苗");
        List<String> reimuExpression = Arrays.asList("默认", "龇牙", "互动", "微笑", "気持", "思考向上", "GJ", "汗颜", "思考");
        List<String> sanaeExpression = Arrays.asList("默认", "开心", "猫耳", "互动", "汗颜", "好奇", "汇报");

        List<RecommendTalk> recommendTalkList = new ArrayList<>();
        for (Integer index = 0; index < talkList.length; index++) {
            String talk = talkList[index];
            String speaker = StringUtil.matcherGroupOne("^(.+?)：", talk);
            String text = StringUtil.matcherGroupOne("：(.+)（", talk);
            String expression = StringUtil.matcherGroupOne("([^（]+)）$", talk);
            Asserts.notNull(speaker, "%s第%s行，无法识别主持人", area, index);
            Asserts.notNull(text, "%s第%s行，无法识别台本", area, index);
            Asserts.notNull(expression, "%s第%s行，无法识别表情", area, index);

            Asserts.isTrue(speakerList.contains(speaker), "%s第%s行，没有角色%s", area, index, speaker);
            if (Objects.equals(speaker, "灵梦")) {
                Asserts.isTrue(reimuExpression.contains(expression), "%s第%s行，%s没有表情%s", area, index, speaker, expression);
            } else if (Objects.equals(speaker, "早苗")) {
                Asserts.isTrue(sanaeExpression.contains(expression), "%s第%s行，%s没有表情%s", area, index, speaker, expression);
            }

            RecommendTalk recommendTalk = new RecommendTalk().setSpeaker(speaker).setText(text).setExpression(expression).setType(type).setIssueId(issueId);
            recommendTalkList.add(recommendTalk);
        }
        recommendTalkManager.batchDeleteAndAdd(issueId, type, recommendTalkList);
    }

}
