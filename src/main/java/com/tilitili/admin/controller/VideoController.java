package com.tilitili.admin.controller;

import com.tilitili.common.entity.Video;
import com.tilitili.common.entity.query.VideoQuery;
import com.tilitili.admin.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("api/video")
@Validated
@Slf4j
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/issue/{issue}/data.txt")
    public ResponseEntity<String> getVideoDataFile(@PathVariable int issue) {
        return ResponseEntity.ok(videoService.getVideoDataFile(issue));
    }

}
