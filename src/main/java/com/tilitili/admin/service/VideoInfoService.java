package com.tilitili.admin.service;

import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.mapper.VideoInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class VideoInfoService {

    private final VideoInfoMapper videoInfoMapper;

    public VideoInfoService(VideoInfoMapper videoInfoMapper) {
        this.videoInfoMapper = videoInfoMapper;
    }

    public void delete(Long av) {
        videoInfoMapper.update(new VideoInfo().setAv(av).setIsDelete(true));
    }

    public void recovery(Long av) {
        videoInfoMapper.update(new VideoInfo().setAv(av).setIsDelete(false));
    }

    public void updateStartTime(Long av, Long startTime) {
        videoInfoMapper.update(new VideoInfo().setAv(av).setStartTime(startTime));
    }

}
