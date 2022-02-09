package com.tilitili.admin.controller;

import com.tilitili.admin.entity.count.VideoDataCountResponse;
import com.tilitili.admin.service.VideoDataService;
import com.tilitili.common.entity.dto.VideoDTO;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.rank.VideoDataMapper;
import com.tilitili.common.utils.Asserts;
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
    private final VideoDataMapper videoDataMapper;
    private final VideoDataService videoDataService;

    @Autowired
    public VideoDataController(VideoDataMapper videoDataMapper, VideoDataService videoDataService) {
        this.videoDataMapper = videoDataMapper;
        this.videoDataService = videoDataService;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel<PageModel<VideoDTO>> getVideoDataByCondition(VideoDataQuery query) {
        Asserts.notNull(query, "参数异常");

        if (query.getSorted() == null) query.setSorted("desc");
        if (query.getPageNo() == null) query.setPageNo(1);
        if (query.getPageSize() == null) query.setPageSize(20);

        int count = videoDataMapper.countForVideoDataTable(query);
        List<VideoDTO> videoDataList = videoDataMapper.listForVideoDataTable(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), videoDataList);
    }

    @GetMapping("/count")
    @ResponseBody
    public BaseModel<VideoDataCountResponse> getVideoDataCount(VideoDataQuery query) {
        Asserts.notNull(query, "参数异常");

        if (query.getSorted() == null) query.setSorted("desc");
        if (query.getPageNo() == null) query.setPageNo(1);
        if (query.getPageSize() == null) query.setPageSize(20);

        VideoDataCountResponse videoDataCountResponse = new VideoDataCountResponse();
        videoDataCountResponse.setVideoDataAddCountList(videoDataService.getVideoDataCount(query));
        return BaseModel.success(videoDataCountResponse);
    }

    @PatchMapping("/rank")
    @ResponseBody
    public BaseModel<?> reRank(@RequestBody Integer issue) {
        Asserts.notNull(issue, "参数异常");
        int newIssue = videoDataMapper.getNewIssue();
        if (issue != newIssue) {
            return new BaseModel<>("只有最新一期能重排");
        }
        videoDataService.reRank(issue);
        return new BaseModel<>("成功", true);
    }

}
