package com.tilitili.admin.controller;

import com.tilitili.admin.service.RecommendService;
import com.tilitili.common.entity.Admin;
import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.manager.ResourcesManager;
import com.tilitili.common.mapper.RecommendMapper;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tilitili.admin.utils.BilibiliUtil.converseAvToBv;

@Slf4j
@Controller
@RequestMapping("/api/recommend")
public class RecommendController extends BaseController {
    private final RecommendMapper recommendMapper;
    private final RecommendService recommendService;
    private final ResourcesManager resourcesManager;

    @Autowired
    public RecommendController(RecommendMapper recommendMapper, RecommendService recommendService, ResourcesManager resourcesManager) {
        this.recommendMapper = recommendMapper;
        this.recommendService = recommendService;
        this.resourcesManager = resourcesManager;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getUseRecommendByCondition(RecommendQuery query) {
        Asserts.notNull(query, "参数");
        query.setStatus(1);
        int count = recommendMapper.count(query);
        List<Recommend> recommendList = recommendService.list(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), recommendList);
    }

    @GetMapping("/pool")
    @ResponseBody
    public BaseModel getRecommendPoolByCondition(RecommendQuery query) {
        Asserts.notNull(query, "参数");
        query.setStatus(0);
        int count = recommendMapper.count(query);
        List<Recommend> recommendList = recommendService.list(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), recommendList);
    }

    @PostMapping("")
    @ResponseBody
    public BaseModel addRecommend(@RequestBody Recommend recommend, @SessionAttribute(value = "admin", required = false) Admin admin) {
        if (recommend.getBv() != null) {
            recommend.setAv(converseAvToBv(recommend.getBv()));
        }

        Asserts.notNull(recommend.getAv(), "av号");
        Asserts.notNull(admin.getUserName(), "操作人");

        recommend.setOperator(admin.getUserName());

        Recommend oldRecommend = recommendMapper.getByAv(recommend.getAv());
        Asserts.checkNull(oldRecommend, "该视频推荐已存在");

        recommendMapper.insert(recommend);
        return new BaseModel("推荐成功",true);
    }

    @PatchMapping("")
    @ResponseBody
    public BaseModel updateRecommend(@RequestBody Recommend recommend) {
        Asserts.notNull(recommend.getId(), "av号");

        Recommend updateRecommend = new Recommend();
        updateRecommend.setId(recommend.getId());
        updateRecommend.setStatus(-1);
        recommendMapper.update(updateRecommend);

        return new BaseModel("废弃成功",true);
    }

    @PatchMapping("/status")
    @ResponseBody
    public BaseModel useRecommend(@RequestBody Recommend recommend) {
        Asserts.notNull(recommend.getId(), "av号");

        Recommend updateRecommend = new Recommend();
        updateRecommend.setId(recommend.getId());
        updateRecommend.setStatus(1);
        updateRecommend.setIssue(resourcesManager.getRecommendIssue());
        recommendMapper.update(updateRecommend);

        return new BaseModel("使用成功",true);
    }
}
