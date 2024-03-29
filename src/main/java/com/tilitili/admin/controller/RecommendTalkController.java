package com.tilitili.admin.controller;

import com.google.common.collect.ImmutableMap;
import com.tilitili.admin.entity.view.RecommendTalkView;
import com.tilitili.admin.service.RecommendTalkService;
import com.tilitili.common.entity.RecommendTalk;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.query.RecommendTalkQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.rank.RecommendTalkMapper;
import com.tilitili.common.mapper.rank.RecommendVideoMapper;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/recommendTalk")
public class RecommendTalkController extends BaseController {
    private final RecommendTalkMapper recommendTalkMapper;
    private final RecommendTalkService recommendTalkService;
    private final RecommendVideoMapper recommendVideoMapper;

    @Autowired
    public RecommendTalkController(RecommendTalkMapper recommendTalkMapper, RecommendTalkService recommendTalkService, RecommendVideoMapper recommendVideoMapper) {
        this.recommendTalkMapper = recommendTalkMapper;
        this.recommendTalkService = recommendTalkService;
        this.recommendVideoMapper = recommendVideoMapper;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel<PageModel<RecommendTalk>> getRecommendTalkByCondition(RecommendTalkQuery query) {
        if (query.getIssueId() == null) {
            RecommendVideo recommendVideo = recommendVideoMapper.getNew();
            query.setIssueId(recommendVideo.getId());
        }
        int total = recommendTalkMapper.countRecommendTalkByCondition(query);
        List<RecommendTalk> list = recommendTalkMapper.getRecommendTalkByCondition(query);
        Map<Object, Object> data = ImmutableMap.of(
                "op", recommendTalkService.getTotalNewRecommendTalk(1),
                "ed", recommendTalkService.getTotalNewRecommendTalk(3)
        );
        return PageModel.of(total, query.getPageSize(), query.getCurrent(), list, data);
    }

    @PatchMapping("/batch")
    @ResponseBody
    @Transactional(transactionManager = "rankTransactionManager")
    public BaseModel<?> batchUpdateRecommendTalk(@RequestBody RecommendTalkView recommendTalk) {
        Asserts.notNull(recommendTalk.getOp(), "op未获取到");
        Asserts.notNull(recommendTalk.getEd(), "ed未获取到");
        recommendTalkService.batchUpdate(recommendTalk.getOp(), 1);
        recommendTalkService.batchUpdate(recommendTalk.getEd(), 3);
        return BaseModel.success();
    }

    @PatchMapping("")
    @ResponseBody
    public BaseModel<?> updateRecommendTalkVoice(@RequestBody RecommendTalk recommendTalk) {
        Asserts.notNull(recommendTalk, "参数异常");
        Asserts.notNull(recommendTalk.getId(), "参数异常");
        if (StringUtils.isBlank(recommendTalk.getVoiceUrl())) recommendTalkMapper.deleteRecommendTalkVoiceUrlById(recommendTalk.getId());
        recommendTalkMapper.updateRecommendTalkSelective(recommendTalk);
        return BaseModel.success();
    }
}
