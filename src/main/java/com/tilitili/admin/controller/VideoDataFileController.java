package com.tilitili.admin.controller;

import com.tilitili.admin.entity.VideoDataFileItem;
import com.tilitili.admin.entity.VideoDataFileItemV2;
import com.tilitili.admin.service.VideoDataFileService;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/data/file")
    @ResponseBody
    public BaseModel getVideoDataList(VideoDataQuery videoDataQuery) {
        List<VideoDataFileItem> videoDataFileItemList = videoDataFileService.listForDataFile(videoDataQuery);
        return PageModel.of(videoDataFileItemList.size(), videoDataFileItemList.size(), 1, videoDataFileItemList);
    }

    @GetMapping("/data/fileV2")
    @ResponseBody
    public BaseModel getVideoDataListV2(VideoDataQuery videoDataQuery) {
        List<VideoDataFileItemV2> videoDataFileItemList = videoDataFileService.listForDataFileV2(videoDataQuery);
        return PageModel.of(videoDataFileItemList.size(), videoDataFileItemList.size(), 1, videoDataFileItemList);
    }

//    @GetMapping("/av/{av}")
//    public BaseModel listVideo(@PathVariable Long av, VideoQuery videoQuery) {
//        videoQuery.setAv(av);
//        List<Video> videoList = videoService.listVideo(videoQuery);
//        return PageModel.of(0, videoQuery.getPageSize(), videoQuery.getCurrent(), videoList);
//    }

}
