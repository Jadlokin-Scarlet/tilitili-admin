package com.tilitili.admin.controller;

import com.tilitili.common.entity.RecommendTalk;
import com.tilitili.common.entity.query.RecommendTalkQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.RecommendTalkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/recommendTalk")
public class RecommendTalkController extends BaseController {
    private final RecommendTalkMapper recommendTalkMapper;

    @Autowired
    public RecommendTalkController(RecommendTalkMapper recommendTalkMapper) {
        this.recommendTalkMapper = recommendTalkMapper;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getRecommendTalkByCondition(RecommendTalkQuery query) {
        int total = recommendTalkMapper.count(query);
        List<RecommendTalk> list = recommendTalkMapper.list(query);
        return PageModel.of(total, query.getPageSize(), query.getCurrent(), list);
    }

}
