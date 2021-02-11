package com.tilitili.admin.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tilitili.admin.entity.VideoDataFileItem;
import com.tilitili.admin.service.VideoDataFileService;
import com.tilitili.common.entity.Video;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("api/video")
@Validated
@Slf4j
public class VideoDataFileController extends BaseController {

    private final VideoDataFileService videoDataFileService;

    @Autowired
    public VideoDataFileController(VideoDataFileService videoDataFileService) {
        this.videoDataFileService = videoDataFileService;
    }

//    @GetMapping("/issue/{issue}/data.txt")
//    public ResponseEntity<String> getVideoDataFile(@PathVariable int issue) {
//        return ResponseEntity.ok(videoDataFileService.getVideoDataFile(issue));
//    }

    @GetMapping("/data/file")
    @ResponseBody
    @JsonView(VideoDataFileItem.VideoView.class)
    public BaseModel getVideoDataList(VideoDataQuery videoDataQuery) {
        List<VideoDataFileItem> videoDataFileItemList = videoDataFileService.listForDataFile(videoDataQuery);
        return PageModel.of(100, videoDataQuery.getPageSize(), videoDataQuery.getCurrent(), videoDataFileItemList);
    }

    @GetMapping("/data/adminFile")
    @ResponseBody
    @JsonView(VideoDataFileItem.AdminView.class)
    public BaseModel getAdminVideoDataList(VideoDataQuery videoDataQuery) {
        List<VideoDataFileItem> videoDataFileItemList = videoDataFileService.listForDataFile(videoDataQuery);
        return PageModel.of(100, videoDataQuery.getPageSize(), videoDataQuery.getCurrent(), videoDataFileItemList);
    }

//    @GetMapping("/av/{av}")
//    public BaseModel listVideo(@PathVariable Long av, VideoQuery videoQuery) {
//        videoQuery.setAv(av);
//        List<Video> videoList = videoService.listVideo(videoQuery);
//        return PageModel.of(0, videoQuery.getPageSize(), videoQuery.getCurrent(), videoList);
//    }

}
