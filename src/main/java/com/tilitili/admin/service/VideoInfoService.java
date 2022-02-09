package com.tilitili.admin.service;

import com.tilitili.admin.entity.count.VideoInfoCountRequest;
import com.tilitili.admin.entity.count.sub.NewVideoCount;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.query.VideoInfoQuery;
import com.tilitili.common.manager.VideoInfoManager;
import com.tilitili.common.mapper.rank.VideoDataMapper;
import com.tilitili.common.mapper.rank.VideoInfoMapper;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VideoInfoService {
    private final VideoDataService videoDataService;
    private final VideoInfoMapper videoInfoMapper;
    private final VideoDataMapper videoDataMapper;
    private final VideoInfoManager videoInfoManager;

    public VideoInfoService(VideoDataService videoDataService, VideoInfoMapper videoInfoMapper, VideoDataMapper videoDataMapper, VideoInfoManager videoInfoManager) {
        this.videoDataService = videoDataService;
        this.videoInfoMapper = videoInfoMapper;
        this.videoDataMapper = videoDataMapper;
        this.videoInfoManager = videoInfoManager;
    }

    public void delete(Long av) {
        videoInfoMapper.updateVideoInfoSelective(new VideoInfo().setAv(av).setIsDelete(true));
        Integer issue = videoDataMapper.getNewIssue();
        VideoData videoData = videoDataMapper.getVideoDataByAvAndIssue(av, issue);
        if (videoData == null) {
            return;
        }
        if (videoData.getRank() != 0) {
            videoDataService.reRank(issue);
        }
    }

    public void recovery(Long av) {
        videoInfoMapper.updateVideoInfoSelective(new VideoInfo().setAv(av).setIsDelete(false));
        Integer issue = videoDataMapper.getNewIssue();
        VideoData videoData = videoDataMapper.getVideoDataByAvAndIssue(av, issue);
        VideoData theLastVideoData = videoDataMapper.getTheLastVideoDataByIssue(issue, VideoDataService.RANK_LIMIT);
        if (videoData == null) {
            return;
        }
        if (theLastVideoData == null || videoData.getPoint() >= theLastVideoData.getPoint()) {
            videoDataService.reRank(issue);
        }
    }

    public void updateStartTime(Long av, Long startTime) {
        videoInfoMapper.updateVideoInfoSelective(new VideoInfo().setAv(av).setStartTime(startTime));
    }

    //如果查询前14天的视频增量和涨幅（不含今天）,就查前16天的
    public List<NewVideoCount> getNewVideoCount(VideoInfoCountRequest request) {
        Integer countTime = request.getTime();

        Calendar calendar = Calendar.getInstance();
        Date createTimeEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, - countTime);
        Date createTimeStart = calendar.getTime();

        VideoInfoQuery videoInfoQuery = new VideoInfoQuery().setStatus(0).setIsDelete(false).setPubTimeStart(createTimeStart).setPubTimeEnd(createTimeEnd).setType(request.getType()).setPageNo(1).setPageSize(20);
        Map<String, Integer> groupMap = videoInfoManager.groupByPubTime(videoInfoQuery);

        VideoInfoQuery videoInfoQuery1 = new VideoInfoQuery().setPubTimeEnd(createTimeStart).setType(request.getType());
        int count = videoInfoMapper.countVideoInfoWithPubTime(videoInfoQuery1);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<NewVideoCount> newVideoCountList = new ArrayList<>();
        while (countTime --> 0) {
            String time = df.format(calendar.getTime());
            Integer number = groupMap.getOrDefault(time, 0);

            count += number;
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            NewVideoCount newVideoCount = new NewVideoCount().setTime(time).setNewVideoNum(count).setNewVideoAdd(number);
            newVideoCountList.add(newVideoCount);
        }
        return newVideoCountList;
    }

}
