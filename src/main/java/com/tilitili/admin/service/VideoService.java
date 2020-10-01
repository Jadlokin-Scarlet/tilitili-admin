package com.tilitili.admin.service;

import com.tilitili.admin.entity.Video;
import com.tilitili.admin.manager.VideoManager;
import com.tilitili.admin.mapper.VideoMapper;
import com.tilitili.admin.query.VideoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {

    private final VideoMapper videoMapper;
    private final VideoManager videoManager;

    private List<String> fields = Arrays.asList("av", "name", "img", "type", "owner",
            "copyright", "pubTime", "startTime", "view", "reply",
            "favorite", "coin", "point", "rank", "hisRank", "isLen");

    @Autowired
    public VideoService(VideoMapper videoMapper, VideoManager videoManager) {
        this.videoMapper = videoMapper;
        this.videoManager = videoManager;
    }

    public String getVideoDataFile(int issue) {
        VideoQuery videoQuery = new VideoQuery().setTop(30).setIssue(issue).setSorter("point").setSorted("desc");
        String head = String.join("\t", fields) + "\n";
        String body = videoManager.listTopVideo(videoQuery).stream()
                .map(video -> video.toString(fields))
                .collect(Collectors.joining("\n"));
        return head + body;
    }

    public List<Video> listVideo(VideoQuery videoQuery) {
        return videoManager.listTopVideo(videoQuery);
    }
}
