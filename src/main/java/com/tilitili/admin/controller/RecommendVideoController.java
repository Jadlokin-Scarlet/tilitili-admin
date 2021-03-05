package com.tilitili.admin.controller;

import com.tilitili.admin.service.RecommendVideoService;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.query.RecommendVideoQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.RecommendVideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/recommendVideo")
public class RecommendVideoController extends BaseController {
    private final RecommendVideoMapper recommendVideoMapper;
    private final RecommendVideoService recommendVideoService;

    @Autowired
    public RecommendVideoController(RecommendVideoMapper recommendVideoMapper, RecommendVideoService recommendVideoService) {
        this.recommendVideoMapper = recommendVideoMapper;
        this.recommendVideoService = recommendVideoService;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getRecommendVideoByCondition(RecommendVideoQuery query) {
        int count = recommendVideoMapper.count(query);
        List<RecommendVideo> recommendVideoList = recommendVideoService.list(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), recommendVideoList);
    }

    @PostMapping("")
    @ResponseBody
    public BaseModel addRecommendVideo(@RequestBody RecommendVideo recommendVideo) {
        recommendVideoMapper.insert(recommendVideo);
        return BaseModel.success();
    }
}
