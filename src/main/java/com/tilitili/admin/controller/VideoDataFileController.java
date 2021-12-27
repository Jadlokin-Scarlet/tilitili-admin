package com.tilitili.admin.controller;

import com.tilitili.admin.entity.VideoDataAdminFileItem;
import com.tilitili.admin.service.VideoDataFileService;
import com.tilitili.admin.service.VideoDataService;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.tilitili.VideoDataMapper;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("api/video")
@Validated
@Slf4j
public class VideoDataFileController extends BaseController {

    private final VideoDataFileService videoDataFileService;
    private final VideoDataMapper videoDataMapper;

    @Autowired
    public VideoDataFileController(VideoDataFileService videoDataFileService, VideoDataMapper videoDataMapper) {
        this.videoDataFileService = videoDataFileService;
        this.videoDataMapper = videoDataMapper;
    }

    @GetMapping("/data/adminFile")
    @ResponseBody
    public BaseModel<?> getAdminVideoDataList(VideoDataQuery query) {
        Asserts.notNull(query, "参数异常");

        if (query.getIssue() == null) {
            query.setIssue(videoDataMapper.getNewIssue());
        }

        int hasRankCnt = videoDataMapper.countForDataFile(query.getIssue());
        Asserts.checkEquals(hasRankCnt, VideoDataService.RANK_LIMIT, "未排行，请先排行");

        List<VideoDataAdminFileItem> dataFile = videoDataFileService.listForDataFile(query);
        return PageModel.of(dataFile.size(), dataFile.size(), 1, dataFile);
    }

}
