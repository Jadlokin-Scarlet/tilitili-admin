package com.tilitili.admin.controller;

import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.query.VideoInfoQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.admin.service.VideoInfoService;
import com.tilitili.common.mapper.VideoInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/video/info")
@Validated
@Slf4j
public class VideoInfoController extends BaseController {

    private final VideoInfoService videoInfoService;
    private final VideoInfoMapper videoInfoMapper;

    @Autowired
    public VideoInfoController(VideoInfoService videoInfoService, VideoInfoMapper videoInfoMapper) {
        this.videoInfoService = videoInfoService;
        this.videoInfoMapper = videoInfoMapper;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getVideoInfoByCondition(VideoInfoQuery query) {
        int count = videoInfoMapper.count(query);
        List<VideoInfo> videoInfoList = videoInfoMapper.list(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), videoInfoList);
    }

    @DeleteMapping("/{av}/isDelete/true")
    public BaseModel deleteVideo(@PathVariable Long av) {
        videoInfoService.delete(av);
        return new BaseModel("成功删除", true);
    }

    @PatchMapping("/{av}/isDelete/false")
    public BaseModel recoveryVideo(@PathVariable Long av) {
        videoInfoService.recovery(av);
        return new BaseModel("成功恢复", true);
    }

    @PatchMapping("/{av}/startTime/{startTime}")
    public BaseModel updateStartTime(@PathVariable Long av, @PathVariable Long startTime) {
        videoInfoService.updateStartTime(av, startTime);
        return new BaseModel("成功恢复", true);
    }

}
