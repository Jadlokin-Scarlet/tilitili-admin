package com.tilitili.admin.manager;

import com.tilitili.admin.entity.Video;
import com.tilitili.admin.mapper.VideoMapper;
import com.tilitili.admin.query.VideoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VideoManager {

    private final VideoMapper videoMapper;

    @Autowired
    public VideoManager(VideoMapper videoMapper) {
        this.videoMapper = videoMapper;
    }

    public List<Video> listVideo(VideoQuery videoQuery) {
        return videoMapper.list(videoQuery);
    }

    public List<Video> listTopVideo(VideoQuery videoQuery) {
        return videoMapper.list(videoQuery.setSorter("point").setSorted("desc"));
    }
}
