package com.tilitili.admin.controller;

import com.tilitili.admin.entity.count.VideoDataCountResponse;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.manager.VideoDataManager;
import com.tilitili.admin.service.VideoDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/video/data")
@Validated
@Slf4j
public class VideoDataController extends BaseController {

    private final VideoDataManager videoDataManager;
    private final VideoDataService videoDataService;

    @Autowired
    public VideoDataController(VideoDataManager videoDataManager, VideoDataService videoDataService) {
        this.videoDataManager = videoDataManager;
        this.videoDataService = videoDataService;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getVideoDataByCondition(VideoDataQuery query) {
        int count = videoDataManager.count(query);
        List<VideoData> videoDataList = videoDataManager.list(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), videoDataList);
    }

    @GetMapping("/count")
    @ResponseBody
    public BaseModel getVideoDataCount(VideoDataQuery query) {
        VideoDataCountResponse videoDataCountResponse = new VideoDataCountResponse();
        videoDataCountResponse.setVideoDataAddCountList(videoDataService.getVideoDataCount(query));
        return BaseModel.success(videoDataCountResponse);
    }

    @PatchMapping("/rank")
    @ResponseBody
    public BaseModel reRank(Integer issue) {
        int newIssue = videoDataManager.listIssue().stream()
                .mapToInt(Integer::intValue).max().getAsInt();
        if (issue != newIssue) {
            return new BaseModel("只有最新一期能重排");
        }
        videoDataService.reRank(issue);
//        videoDataManager.listIssue().forEach(videoDataService::reRank);
        return new BaseModel("成功", true);
    }

}
