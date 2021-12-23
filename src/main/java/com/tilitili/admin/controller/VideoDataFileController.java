package com.tilitili.admin.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tilitili.admin.entity.VideoDataFileItem;
import com.tilitili.admin.service.VideoDataFileService;
import com.tilitili.admin.service.VideoDataService;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.manager.VideoDataManager;
import com.tilitili.common.utils.Asserts;
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

    private final VideoDataManager videoDataManager;

    @Autowired
    public VideoDataFileController(VideoDataFileService videoDataFileService, VideoDataManager videoDataManager) {
        this.videoDataFileService = videoDataFileService;
        this.videoDataManager = videoDataManager;
    }

    @GetMapping("/data/adminFile")
    @ResponseBody
    public BaseModel<?> getAdminVideoDataList(VideoDataQuery query) {
        Asserts.notNull(query, "参数异常");

        if (query.getIssue() == null) {
            query.setIssue(videoDataManager.getNewIssue());
        }

        Integer rankLength = videoDataManager.countForDataFile(query.getIssue());
        Asserts.notEquals(rankLength, 0, "未排行，请先排行");
        Asserts.isTrue(rankLength > 100, "未排行完毕，请稍等");

        return videoDataFileService.listForDataFile(query);
    }

}
