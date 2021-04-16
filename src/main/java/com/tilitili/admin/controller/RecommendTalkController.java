package com.tilitili.admin.controller;

import com.google.common.collect.ImmutableMap;
import com.tilitili.admin.service.RecommendTalkService;
import com.tilitili.common.entity.RecommendTalk;
import com.tilitili.common.entity.query.RecommendTalkQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.RecommendTalkMapper;
import com.tilitili.common.utils.Asserts;
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

    @Autowired
    public RecommendTalkController(RecommendTalkMapper recommendTalkMapper, RecommendTalkService recommendTalkService) {
        this.recommendTalkMapper = recommendTalkMapper;
        this.recommendTalkService = recommendTalkService;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getRecommendTalkByCondition(RecommendTalkQuery query) {
        int total = recommendTalkMapper.count(query);
        List<RecommendTalk> list = recommendTalkMapper.list(query);
        Map<Object, Object> data = ImmutableMap.of(
                "op", recommendTalkService.getTotalNewRecommendTalk(1),
                "ed", recommendTalkService.getTotalNewRecommendTalk(3)
        );
        return PageModel.of(total, query.getPageSize(), query.getCurrent(), list, data);
    }

    @PatchMapping("")
    @ResponseBody
    @Transactional
    public BaseModel batchUpdateRecommendTalk(@RequestBody RecommendTalk recommendTalk) {
        Asserts.notNull(recommendTalk.getOp(), "op未获取到");
        Asserts.notNull(recommendTalk.getEd(), "ed未获取到");
        recommendTalkService.batchUpdate(recommendTalk.getOp(), 1);
        recommendTalkService.batchUpdate(recommendTalk.getEd(), 3);
        return BaseModel.success();
    }
}
