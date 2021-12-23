package com.tilitili.admin.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tilitili.admin.entity.VideoDataFileItem;
import com.tilitili.admin.service.VideoDataFileService;
import com.tilitili.admin.service.VideoDataService;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.manager.VideoDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/video")
@Validated
@Slf4j
public class VideoDataFileController extends BaseController {

    private final VideoDataFileService videoDataFileService;
    private final VideoDataService videoDataService;

    private final VideoDataManager videoDataManager;

    @Autowired
    public VideoDataFileController(VideoDataFileService videoDataFileService, VideoDataService videoDataService, VideoDataManager videoDataManager) {
        this.videoDataFileService = videoDataFileService;
        this.videoDataManager = videoDataManager;
        this.videoDataService = videoDataService;
    }

    @GetMapping("/data/adminFile")
    @ResponseBody
    @JsonView(VideoDataFileItem.AdminView.class)
    public BaseModel getAdminVideoDataList(VideoDataQuery videoDataQuery) {
        if (videoDataQuery.getIssue() == null) {
            videoDataQuery.setIssue(videoDataManager.getNewIssue());
        }
        if (! videoDataService.isRank(videoDataQuery.getIssue())) {
            return new BaseModel("未排行，请先排行");
        }
        return videoDataFileService.listForDataFile(videoDataQuery);
    }

}
