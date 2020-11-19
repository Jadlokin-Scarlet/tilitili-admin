package com.tilitili.admin.controller;

import com.tilitili.admin.entity.VideoDataFileItem;
import com.tilitili.admin.service.VideoDataFileService;
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

    @GetMapping("/issue/{issue}/data.txt")
    public ResponseEntity<String> getVideoDataFile(@PathVariable int issue) {
        return ResponseEntity.ok(videoDataFileService.getVideoDataFile(issue));
    }

    @GetMapping("/issue/{issue}/data")
    @ResponseBody
    public BaseModel getVideoDataList(@PathVariable int issue) {
        List<VideoDataFileItem> videoDataFileItemList = videoDataFileService.listForDataFile(issue);
        return PageModel.of(100, 100, 1, videoDataFileItemList);
    }

//    @GetMapping("/av/{av}")
//    public BaseModel listVideo(@PathVariable Long av, VideoQuery videoQuery) {
//        videoQuery.setAv(av);
//        List<Video> videoList = videoService.listVideo(videoQuery);
//        return PageModel.of(0, videoQuery.getPageSize(), videoQuery.getCurrent(), videoList);
//    }

}
