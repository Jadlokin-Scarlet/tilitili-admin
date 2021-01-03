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
import org.springframework.util.Assert;
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
    @ResponseBody
    public BaseModel deleteVideo(@PathVariable Long av) {
        videoInfoService.delete(av);
        return new BaseModel("成功删除", true);
    }

    @PatchMapping("/{av}/isDelete/false")
    @ResponseBody
    public BaseModel recoveryVideo(@PathVariable Long av) {
        videoInfoService.recovery(av);
        return new BaseModel("成功恢复", true);
    }

    @PatchMapping("")
    @ResponseBody
    public BaseModel updateStartTime(@RequestBody VideoInfo videoInfo) {
        Assert.notNull(videoInfo, "参数异常");
        Assert.notNull(videoInfo.getAv(), "参数异常");
        Assert.notNull(videoInfo.getStartTime(), "参数异常");
        videoInfoService.updateStartTime(videoInfo.getAv(), videoInfo.getStartTime());
        return new BaseModel("更新成功", true);
    }

}
