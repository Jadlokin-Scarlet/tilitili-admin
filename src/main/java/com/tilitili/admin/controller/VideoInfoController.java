package com.tilitili.admin.controller;

import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.query.VideoInfoQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.VideoInfoMapper;
import com.tilitili.admin.service.VideInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/video/info")
@Validated
@Slf4j
public class VideoInfoController {

    private final VideoInfoMapper videoInfoMapper;

    @Autowired
    public VideoInfoController(VideoInfoMapper videoInfoMapper) {
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
        videoInfoMapper.delete(av);
        return new BaseModel("成功删除", true);
    }

    @PatchMapping("/{av}/isDelete/false")
    public BaseModel recoveryVideo(@PathVariable Long av) {
        videoInfoMapper.recovery(av);
        return new BaseModel("成功恢复", true);
    }

}
