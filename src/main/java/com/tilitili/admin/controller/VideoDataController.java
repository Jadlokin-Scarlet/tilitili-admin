package com.tilitili.admin.controller;

import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.mapper.VideoDataMapper;
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
public class VideoDataController {

    private final VideoDataMapper videoDataMapper;
    private final VideoDataService videoDataService;

    @Autowired
    public VideoDataController(VideoDataMapper videoDataMapper, VideoDataService videoDataService) {
        this.videoDataMapper = videoDataMapper;
        this.videoDataService = videoDataService;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getVideoDataByCondition(VideoDataQuery query) {
        int count = videoDataMapper.countVideoByCondition(query);
        List<VideoData> videoDataList = videoDataMapper.listVideoByCondition(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), videoDataList);
    }

    @PatchMapping("/rank")
    @ResponseBody
    public BaseModel reRank(@RequestBody Integer issue) {
        int newIssue = videoDataMapper.listIssue().stream()
                .mapToInt(Integer::intValue).max().getAsInt();
        if (issue != newIssue) {
            return new BaseModel("只有最新一期能重排");
        }
        videoDataService.reRank(issue);
        return new BaseModel("成功", true);
    }

}
