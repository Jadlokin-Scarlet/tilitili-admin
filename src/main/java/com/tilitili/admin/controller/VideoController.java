package com.tilitili.admin.controller;

import com.tilitili.admin.service.VideoDataFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/video")
@Validated
@Slf4j
public class VideoController {

    private final VideoDataFileService videoDataFileService;

    @Autowired
    public VideoController(VideoDataFileService videoDataFileService) {
        this.videoDataFileService = videoDataFileService;
    }

    @GetMapping("/issue/{issue}/data.txt")
    public ResponseEntity<String> getVideoDataFile(@PathVariable int issue) {
        return ResponseEntity.ok(videoDataFileService.getVideoDataFile(issue));
    }

//    @GetMapping("/av/{av}")
//    public BaseModel listVideo(@PathVariable Long av, VideoQuery videoQuery) {
//        videoQuery.setAv(av);
//        List<Video> videoList = videoService.listVideo(videoQuery);
//        return PageModel.of(0, videoQuery.getPageSize(), videoQuery.getCurrent(), videoList);
//    }

}
