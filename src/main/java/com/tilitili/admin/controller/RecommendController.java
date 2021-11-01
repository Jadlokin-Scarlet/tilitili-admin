package com.tilitili.admin.controller;

import com.tilitili.admin.service.RecommendService;
import com.tilitili.common.entity.Admin;
import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.entity.view.SimpleTask;
import com.tilitili.common.manager.RecommendManager;
import com.tilitili.common.manager.ResourcesManager;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.mapper.tilitili.RecommendMapper;
import com.tilitili.common.mapper.tilitili.RecommendVideoMapper;
import com.tilitili.common.mapper.tilitili.VideoInfoMapper;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.BilibiliUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.tilitili.common.emnus.TaskReason.NO_REASON;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Controller
@RequestMapping("/api/recommend")
public class RecommendController extends BaseController {
    private final RecommendMapper recommendMapper;
    private final RecommendManager recommendManager;
    private final RecommendService recommendService;
    private final VideoInfoMapper videoInfoMapper;
    private final RecommendVideoMapper recommendVideoMapper;
    private final TaskManager taskManager;

    @Autowired
    public RecommendController(RecommendMapper recommendMapper, RecommendManager recommendManager, RecommendService recommendService, ResourcesManager resourcesManager, VideoInfoMapper videoInfoMapper, RecommendVideoMapper recommendVideoMapper, TaskManager taskManager) {
        this.recommendMapper = recommendMapper;
        this.recommendManager = recommendManager;
        this.recommendService = recommendService;
        this.videoInfoMapper = videoInfoMapper;
        this.recommendVideoMapper = recommendVideoMapper;
        this.taskManager = taskManager;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getRecommendByCondition(RecommendQuery query) {
        Asserts.notNull(query, "参数异常");
        int count = recommendMapper.count(query);
        List<Recommend> recommendList = recommendMapper.list(query);
        recommendService.supplementRecommend(recommendList);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), recommendList);
    }

    @GetMapping("/use")
    @ResponseBody
    public BaseModel getUseRecommendByCondition(RecommendQuery query) {
        Asserts.notNull(query, "参数异常");
        int count = recommendManager.countUseRecommend(query);
        List<Recommend> recommendList = recommendManager.listUseRecommend(query);
        recommendService.supplementRecommend(recommendList);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), recommendList);
    }

    @GetMapping("/pool")
    @ResponseBody
    public BaseModel getRecommendPoolByCondition(RecommendQuery query) {
        Asserts.notNull(query, "参数异常");
        int count = recommendManager.countRecommendPool(query);
        List<Recommend> recommendList = recommendManager.listRecommendPool(query);
        recommendService.supplementRecommend(recommendList);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), recommendList);
    }

    @GetMapping("/self")
    @ResponseBody
    public BaseModel getSelfRecommendByCondition(RecommendQuery query) {
        Asserts.notNull(query, "参数异常");
        int count = recommendManager.countSelfRecommend(query);
        List<Recommend> recommendList = recommendManager.listSelfRecommend(query);
        recommendService.supplementRecommend(recommendList);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), recommendList);
    }

    @GetMapping("/selfPool")
    @ResponseBody
    public BaseModel getSelfRecommendPoolByCondition(RecommendQuery query) {
        Asserts.notNull(query, "参数异常");
        int count = recommendManager.countSelfRecommendPool(query);
        List<Recommend> recommendList = recommendManager.listSelfRecommendPool(query);
        recommendService.supplementRecommend(recommendList);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), recommendList);
    }

    @PostMapping("")
    @ResponseBody
    public BaseModel addRecommend(@RequestBody Recommend recommend, @SessionAttribute(value = "admin", required = false) Admin admin) {
        if (recommend.getBv() != null) {
            recommend.setAv(BilibiliUtil.converseAvToBv(recommend.getBv()));
        }

        Asserts.notNull(recommend.getAv(), "av号未获取到");
        Asserts.notNull(admin.getUserName(), "操作人未获取到");

        if (isBlank(recommend.getOperator())) {
            recommend.setOperator(admin.getUserName());
        }

        Recommend oldRecommend = recommendMapper.getByAv(recommend.getAv());
        Asserts.checkNull(oldRecommend, "该视频推荐已存在");

        if (isNull(recommend.getStartTime())) {
            recommend.setStartTime(0);
        }
        if (isNull(recommend.getEndTime()) || recommend.getEndTime() == 0) {
            recommend.setEndTime(recommend.getStartTime() + 30);
        }
        Asserts.isTrue(recommend.getStartTime() < recommend.getEndTime(), "开始时间应在结束时间之前");

        if (recommend.getIssue() == null) {
            RecommendVideo newVideo = recommendVideoMapper.getNew();
            recommend.setIssueId(newVideo.getId());
        } else if (recommend.getIssue() != null) {
            RecommendVideo recommendVideo = recommendVideoMapper.getByIssue(recommend.getIssue());
            recommend.setIssueId(recommendVideo.getId());
        }
        if (recommend.getIssueId() != null) {
            recommend.setStatus(1);
        }

        recommendMapper.insert(recommend);
        taskManager.simpleSpiderVideo(new SimpleTask().setReason(NO_REASON.value).setValueList(Arrays.asList(String.valueOf(recommend.getAv()))));
        return new BaseModel("推荐成功",true);
    }

    @PatchMapping("")
    @ResponseBody
    public BaseModel updateRecommend(@RequestBody Recommend recommend) {
        Asserts.notNull(recommend.getId(), "id未获取到");

        Recommend old = recommendMapper.getById(recommend.getId());
        if (recommend.getStartTime() != null || recommend.getEndTime() != null) {
            Integer startTime = recommend.getStartTime() != null? recommend.getStartTime(): old.getStartTime();
            Integer endTime = recommend.getEndTime() != null? recommend.getEndTime(): old.getEndTime();
            Asserts.isTrue(startTime < endTime, "开始时间应在结束时间之前");
            recommend.setStartTime(startTime);
            recommend.setEndTime(endTime);
        }

        recommendMapper.update(recommend);
        if (recommend.getExternalOwner() != null) {
            Long av = recommend.getAv() != null? recommend.getAv(): old.getAv();
            videoInfoMapper.updateExternalOwner(av, recommend.getExternalOwner());
        }

        return new BaseModel("更新成功",true);
    }

    @PatchMapping("/status/-1")
    @ResponseBody
    public BaseModel updateDeleteRecommend(@RequestBody Recommend recommend) {
        Asserts.notNull(recommend.getId(), "av号未获取到");

        Recommend updateRecommend = new Recommend();
        updateRecommend.setId(recommend.getId());
        updateRecommend.setStatus(-1);
        recommendMapper.update(updateRecommend);

        return new BaseModel("废弃成功",true);
    }

    @PatchMapping("/status/0")
    @ResponseBody
    public BaseModel unUseRecommend(@RequestBody Recommend recommend) {
        Asserts.notNull(recommend.getId(), "av号未获取到");
        recommendService.unUseRecommend(recommend.getId());
        return new BaseModel("使用成功",true);
    }

    @PatchMapping("/status/1")
    @ResponseBody
    public BaseModel useRecommend(@RequestBody Recommend recommend) {
        Asserts.notNull(recommend.getId(), "av号未获取到");

        Recommend oldRecommend = recommendMapper.getById(recommend.getId());
        Asserts.notNull(oldRecommend, "推荐信息未获取到");

        VideoInfo videoInfo = videoInfoMapper.getByAv(oldRecommend.getAv());
        Asserts.notNull(videoInfo, "视频信息未获取到，请发起爬取");

        RecommendVideo recommendVideo = recommendVideoMapper.getNew();

        Recommend updateRecommend = new Recommend();
        updateRecommend.setId(recommend.getId());
        updateRecommend.setStatus(1);
        updateRecommend.setIssueId(recommendVideo.getId());
        recommendMapper.update(updateRecommend);

        return new BaseModel("使用成功",true);
    }
}
