package com.tilitili.admin.controller;

import com.tilitili.admin.entity.count.VideoInfoCount;
import com.tilitili.admin.service.BatchTaskService;
import com.tilitili.admin.service.VideoInfoService;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.mapper.VideoInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/video/info/count")
public class VideoInfoCountController {
    private final VideoInfoMapper videoInfoMapper;
    private final VideoInfoService videoInfoService;
    private final BatchTaskService batchTaskService;

    @Autowired
    public VideoInfoCountController(VideoInfoMapper videoInfoMapper, VideoInfoService videoInfoService, BatchTaskService batchTaskService) {
        this.videoInfoMapper = videoInfoMapper;
        this.videoInfoService = videoInfoService;
        this.batchTaskService = batchTaskService;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getVideoInfoCount() {
        VideoInfoCount videoInfoCount = new VideoInfoCount();
        videoInfoCount.setNewVideoCountList(videoInfoService.getNewVideoCount());
        return BaseModel.success(videoInfoCount);
    }
}
