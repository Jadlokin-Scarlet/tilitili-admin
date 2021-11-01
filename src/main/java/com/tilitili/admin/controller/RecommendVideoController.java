package com.tilitili.admin.controller;

import com.tilitili.admin.service.RecommendService;
import com.tilitili.admin.service.RecommendVideoService;
import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.entity.query.RecommendVideoQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.tilitili.RecommendMapper;
import com.tilitili.common.mapper.tilitili.RecommendVideoMapper;
import com.tilitili.common.utils.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/api/recommendVideo")
public class RecommendVideoController extends BaseController {
    private final RecommendMapper recommendMapper;
    private final RecommendService recommendService;
    private final RecommendVideoMapper recommendVideoMapper;
    private final RecommendVideoService recommendVideoService;

    @Autowired
    public RecommendVideoController(RecommendMapper recommendMapper, RecommendService recommendService, RecommendVideoMapper recommendVideoMapper, RecommendVideoService recommendVideoService) {
        this.recommendMapper = recommendMapper;
        this.recommendService = recommendService;
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

    @PatchMapping("")
    @ResponseBody
    public BaseModel updateRecommendVideo(@RequestBody RecommendVideo recommendVideo) {
        Asserts.notNull(recommendVideo.getId(), "id未获取到");
        RecommendVideo newVideo = recommendVideoMapper.getNew();
        Asserts.checkEquals(recommendVideo.getId(), newVideo.getId(), "只有最新一期可以编辑");
        if (Objects.equals(recommendVideo.getType(), 1)) {
            recommendVideo.setIssue(-1);
        }
        if (Objects.equals(recommendVideo.getStatus(), -1)) {
            RecommendQuery recommendQuery = new RecommendQuery().setIssueId(recommendVideo.getId()).setStatus(1);
            List<Recommend> recommendList = recommendMapper.list(recommendQuery);
            for (Recommend recommend : recommendList) {
                recommendService.unUseRecommend(recommend.getId());
            }
        }
        recommendVideoMapper.update(recommendVideo);
        return BaseModel.success();
    }

}
