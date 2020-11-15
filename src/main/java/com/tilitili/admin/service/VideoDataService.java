package com.tilitili.admin.service;

import com.tilitili.common.entity.Resource;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.mapper.VideoDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class VideoDataService {

    private final Integer RANK_LIMIT = 1000;

    private final VideoDataMapper videoDataMapper;

    @Autowired
    public VideoDataService(VideoDataMapper videoDataMapper) {
        this.videoDataMapper = videoDataMapper;
    }

    public List<Resource> getIssueResource() {
        return videoDataMapper.listIssue().stream()
                .map(Resource::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void reRank(int issue) {
        VideoDataQuery videoDataQuery = new VideoDataQuery()
                .setIssue(issue)
                .setIsDelete(false)
                .setStatus(0)
                .setStart(0).setPageSize(RANK_LIMIT)
                .setSorter("point", "desc");
        List<VideoData> videoList = videoDataMapper.list(videoDataQuery);
        IntStream.range(0, RANK_LIMIT)//.parallel()
                .filter(index -> !videoList.get(index).getRank().equals(index + 1))
                .peek(index -> log.info("av{} rank{} update to {}", videoList.get(index).getAv(), videoList.get(index).getRank(), index + 1))
                .mapToObj(index -> videoList.get(index).setRank(index + 1))
                .forEach(videoDataMapper::updateRank);
    }

}
