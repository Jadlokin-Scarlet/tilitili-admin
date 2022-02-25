package com.tilitili.admin.controller;

import com.tilitili.admin.entity.DispatchRecommendResourcesView;
import com.tilitili.admin.entity.DispatchResourcesView;
import com.tilitili.admin.entity.RecommendFileItem;
import com.tilitili.admin.entity.VideoDataFileItem;
import com.tilitili.admin.service.RecommendService;
import com.tilitili.admin.service.ResourceService;
import com.tilitili.admin.service.VideoDataFileService;
import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.RecommendTalk;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.entity.query.RecommendTalkQuery;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.view.resource.Resource;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.manager.RecommendManager;
import com.tilitili.common.mapper.rank.RecommendMapper;
import com.tilitili.common.mapper.rank.RecommendTalkMapper;
import com.tilitili.common.mapper.rank.RecommendVideoMapper;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("api/resources")
@Validated
@Slf4j
public class ResourceController extends BaseController {
    private final ResourceService resourceService;
    private final RecommendManager recommendManager;
    private final RecommendService recommendService;
    private final RecommendMapper recommendMapper;
    private final RecommendVideoMapper recommendVideoMapper;
    private final RecommendTalkMapper recommendTalkMapper;
    private final VideoDataFileService videoDataFileService;

    private List<VideoDataFileItem> cacheDataFile;

    @Autowired
    public ResourceController(RecommendManager recommendManager, ResourceService resourceService, VideoDataFileService videoDataFileService, RecommendService recommendService, RecommendMapper recommendMapper, RecommendVideoMapper recommendVideoMapper, RecommendTalkMapper recommendTalkMapper) {
        this.recommendManager = recommendManager;
        this.resourceService = resourceService;
        this.videoDataFileService = videoDataFileService;
        this.recommendService = recommendService;
        this.recommendMapper = recommendMapper;
        this.recommendVideoMapper = recommendVideoMapper;
        this.recommendTalkMapper = recommendTalkMapper;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel<?> getResources(@RequestParam List<String> needResourcesList) {
        HashMap<String, List<Resource>> resourceMap = new HashMap<>();
        needResourcesList.forEach(
                resourceName -> resourceMap.put(resourceName, resourceService.getResource(resourceName))
        );
        return new BaseModel<>("查询成功", true, resourceMap);
    }

    @GetMapping("/flag")
    @ResponseBody
    public BaseModel<?> getFlag() {
        DispatchResourcesView flagResource = resourceService.getFlagResources();
        flagResource.setMusicName("曲名: " + flagResource.getMusicName());
        flagResource.setMusicOwner("社团: " + flagResource.getMusicOwner());
        flagResource.setMusicCard("专辑: " + flagResource.getMusicCard());
        flagResource.setMusicSource("原曲: " + flagResource.getMusicSource());
        return new BaseModel<>("success", true, flagResource);
    }

    @GetMapping("/recommendFlag")
    @ResponseBody
    public BaseModel<?> getRecommendFlag() {
        DispatchRecommendResourcesView flagResource = resourceService.getRecommendFlagResources();
        return new BaseModel<>("success", true, flagResource);
    }

    @GetMapping("/adminFlag")
    @ResponseBody
    public BaseModel<?> getAdminFlag() {
        DispatchResourcesView flagResource = resourceService.getFlagResources();
        return new BaseModel<>("success", true, flagResource);
    }

    @GetMapping("/videoDataFile")
    @ResponseBody
    public BaseModel<PageModel<VideoDataFileItem>> getVideoDataList(VideoDataQuery query) {
        Asserts.notNull(query, "参数异常");

        if (query.getPageSize() != null && query.getPageNo() != null) {
            Integer pageSize = query.getPageSize();
            Integer pageNo = query.getPageNo();

            if (cacheDataFile == null || pageNo == 1) {
                cacheDataFile = videoDataFileService.toDataFile(videoDataFileService.listForDataFile(query));
            }
            int start = (pageNo - 1) * pageSize;
            List<VideoDataFileItem> result = cacheDataFile.stream().skip(start).limit(pageSize).collect(Collectors.toList());
            return PageModel.of(cacheDataFile.size(), pageSize, pageNo, result);
        }

        List<VideoDataFileItem> dataFile = videoDataFileService.toDataFile(videoDataFileService.listForDataFile(query));
        return PageModel.of(dataFile.size(), dataFile.size(), 1, dataFile);
    }

    @GetMapping("/recommend")
    @ResponseBody
    public BaseModel<PageModel<RecommendFileItem>> getRecommend(RecommendQuery query) {
        if (query.getSorted() == null) query.setSorted("desc");
        if (query.getPageNo() == null) query.setPageNo(1);
        if (query.getPageSize() == null) query.setPageSize(20);

        if (query.getIssueId() == null) {
            RecommendVideo recommendVideo = recommendVideoMapper.getNew();
            query.setIssueId(recommendVideo.getId());
        }
        query.setSorter("sort_num").setSorted("desc").setSubSorter("id").setSubSorted("asc");
        int total = recommendManager.countUseRecommend(query);
        List<RecommendFileItem> recommendList = recommendService.getRecommendFile(query);
        return PageModel.of(total, query.getPageSize(), query.getCurrent(), recommendList);
    }

    @GetMapping("/selfRecommend")
    @ResponseBody
    public BaseModel<PageModel<RecommendFileItem>> getSelfRecommend(RecommendQuery query) {
        if (query.getSorted() == null) query.setSorted("desc");
        if (query.getPageNo() == null) query.setPageNo(1);
        if (query.getPageSize() == null) query.setPageSize(20);

        if (query.getIssueId() == null) {
            RecommendVideo recommendVideo = recommendVideoMapper.getNew();
            query.setIssueId(recommendVideo.getId());
        }
        query.setSorter("sort_num").setSorted("desc").setSubSorter("id").setSubSorted("asc");
        int total = recommendManager.countSelfRecommend(query);
        List<RecommendFileItem> recommendList = recommendService.getSelfRecommendFile(query);
        return PageModel.of(total, query.getPageSize(), query.getCurrent(), recommendList);
    }

    @GetMapping("/recommend/text")
    @ResponseBody
    public BaseModel<?> getRecommendText(Long av) {
        Recommend recommend = recommendMapper.getNormalRecommendByAv(av);
        return BaseModel.success(recommend.getText());
    }

    @GetMapping("/recommendTalk")
    @ResponseBody
    public BaseModel<PageModel<RecommendTalk>> getRecommendTalk(RecommendTalkQuery query) {
        if (query.getSorted() == null) query.setSorted("desc");
        if (query.getPageNo() == null) query.setPageNo(1);
        if (query.getPageSize() == null) query.setPageSize(20);

        if (query.getIssueId() == null) {
            RecommendVideo recommendVideo = recommendVideoMapper.getNew();
            query.setIssueId(recommendVideo.getId());
        }
        query.setStatus(0).setSorter("id").setSorted("asc");
        int total = recommendTalkMapper.countRecommendTalkByCondition(query);
        List<RecommendTalk> recommendTalkList = recommendTalkMapper.getRecommendTalkByCondition(query);
        return PageModel.of(total, query.getPageSize(), query.getCurrent(), recommendTalkList);
    }

    @PatchMapping("/flag")
    @ResponseBody
    public BaseModel<DispatchResourcesView> updateFlag(@RequestBody DispatchResourcesView resourcesView) {
        Asserts.notNull(resourcesView.getIsStaffShow1(), "参数异常");
        Asserts.notNull(resourcesView.getIsStaffShow2(), "参数异常");
        Asserts.notNull(resourcesView.getMusicName(), "参数异常");
        Asserts.notNull(resourcesView.getMusicOwner(), "参数异常");
        Asserts.notNull(resourcesView.getMusicCard(), "参数异常");
        Asserts.notNull(resourcesView.getMusicImage(), "参数异常");
        Asserts.notNull(resourcesView.getMusicSource(), "参数异常");
        Asserts.notNull(resourcesView.getTips(), "参数异常");
        resourceService.updateFlagResources(resourcesView);
        DispatchResourcesView flagResource = resourceService.getFlagResources();
        return new BaseModel<>("保存成功", true, flagResource);
    }
}
