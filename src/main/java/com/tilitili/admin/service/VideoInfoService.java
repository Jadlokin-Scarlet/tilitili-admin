package com.tilitili.admin.service;

import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.mapper.VideoDataMapper;
import com.tilitili.common.mapper.VideoInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VideoInfoService {
    private final VideoDataService videoDataService;

    private final VideoInfoMapper videoInfoMapper;
    private final VideoDataMapper videoDataMapper;

    public VideoInfoService(VideoDataService videoDataService, VideoInfoMapper videoInfoMapper, VideoDataMapper videoDataMapper) {
        this.videoDataService = videoDataService;
        this.videoInfoMapper = videoInfoMapper;
        this.videoDataMapper = videoDataMapper;
    }

    @Transactional
    public void delete(Long av) {
        videoInfoMapper.update(new VideoInfo().setAv(av).setIsDelete(true));
        videoDataMapper.updateRank(new VideoData().setAv(av).setIssue(videoDataMapper.getNewIssue()).setRank(0));
        videoDataService.reRank(videoDataMapper.getNewIssue());
    }

    @Transactional
    public void recovery(Long av) {
        videoInfoMapper.update(new VideoInfo().setAv(av).setIsDelete(false));
        videoDataMapper.updateRank(new VideoData().setAv(av).setIssue(videoDataMapper.getNewIssue()).setRank(0));
        videoDataService.reRank(videoDataMapper.getNewIssue());
    }

    public void updateStartTime(Long av, Long startTime) {
        videoInfoMapper.update(new VideoInfo().setAv(av).setStartTime(startTime));
    }

}
