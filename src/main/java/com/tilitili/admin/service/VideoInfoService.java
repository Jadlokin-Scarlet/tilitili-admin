package com.tilitili.admin.service;

import com.tilitili.admin.entity.count.VideoInfoCountRequest;
import com.tilitili.admin.entity.count.sub.NewVideoCount;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.dto.VideoInfoGroup;
import com.tilitili.common.entity.query.VideoInfoQuery;
import com.tilitili.common.manager.VideoDataManager;
import com.tilitili.common.manager.VideoInfoManager;
import com.tilitili.common.mapper.VideoInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VideoInfoService {
    private final VideoDataService videoDataService;

    private final VideoInfoMapper videoInfoMapper;
    private final VideoDataManager videoDataManager;
    private final VideoInfoManager videoInfoManager;

    public VideoInfoService(VideoDataService videoDataService, VideoInfoMapper videoInfoMapper, VideoDataManager videoDataManager, VideoInfoManager videoInfoManager) {
        this.videoDataService = videoDataService;
        this.videoInfoMapper = videoInfoMapper;
        this.videoDataManager = videoDataManager;
        this.videoInfoManager = videoInfoManager;
    }

    @Transactional
    public void delete(Long av) {
        videoInfoMapper.update(new VideoInfo().setAv(av).setIsDelete(true));
        videoDataManager.updateRank(new VideoData().setAv(av).setIssue(videoDataManager.getNewIssue()).setRank(0));
        videoDataService.reRank(videoDataManager.getNewIssue());
    }

    @Transactional
    public void recovery(Long av) {
        videoInfoMapper.update(new VideoInfo().setAv(av).setIsDelete(false));
        videoDataManager.updateRank(new VideoData().setAv(av).setIssue(videoDataManager.getNewIssue()).setRank(0));
        videoDataService.reRank(videoDataManager.getNewIssue());
    }

    public void updateStartTime(Long av, Long startTime) {
        videoInfoMapper.update(new VideoInfo().setAv(av).setStartTime(startTime));
    }

    //如果查询前14天的视频增量和涨幅（不含今天）,就查前16天的
    public List<NewVideoCount> getNewVideoCount(VideoInfoCountRequest request) {
        Integer countTime = request.getTime();

        Calendar calendar = Calendar.getInstance();
        Date createTimeEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, - countTime);
        Date createTimeStart = calendar.getTime();

        VideoInfoQuery videoInfoQuery = new VideoInfoQuery().setStatus(0).setIsDelete(false).setPubTimeStart(createTimeStart).setPubTimeEnd(createTimeEnd).setType(request.getType());
        Map<String, Integer> groupMap = videoInfoManager.groupByPubTime(videoInfoQuery);

        VideoInfoQuery videoInfoQuery1 = new VideoInfoQuery().setStatus(0).setIsDelete(false).setPubTimeEnd(createTimeStart).setType(request.getType());
        int count = videoInfoMapper.count(videoInfoQuery1);

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
