package com.tilitili.admin.service;

import com.tilitili.admin.entity.count.sub.NewVideoCount;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.dto.VideoInfoGroup;
import com.tilitili.common.entity.query.VideoInfoQuery;
import com.tilitili.common.manager.VideoDataManager;
import com.tilitili.common.mapper.VideoInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class VideoInfoService {
    private final VideoDataService videoDataService;

    private final VideoInfoMapper videoInfoMapper;
    private final VideoDataManager videoDataManager;

    public VideoInfoService(VideoDataService videoDataService, VideoInfoMapper videoInfoMapper, VideoDataManager videoDataManager) {
        this.videoDataService = videoDataService;
        this.videoInfoMapper = videoInfoMapper;
        this.videoDataManager = videoDataManager;
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

    //查询前14天的视频增量和涨幅（不含今天）,所以查前16天的
    public List<NewVideoCount> getNewVideoCount() {
        Calendar calendar = Calendar.getInstance();
        Date createTimeEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -16);
        Date createTimeStart = calendar.getTime();

        VideoInfoQuery videoInfoQuery = new VideoInfoQuery().setStatus(0).setIsDelete(false).setCreateTimeStart(createTimeStart).setCreateTimeEnd(createTimeEnd);
        List<VideoInfoGroup> videoInfoGroupList = videoInfoMapper.groupByCreateDay(videoInfoQuery);

        VideoInfoQuery videoInfoQuery1 = new VideoInfoQuery().setStatus(0).setIsDelete(false).setCreateTimeEnd(createTimeStart);
        int count = videoInfoMapper.count(videoInfoQuery1);

        ArrayList<NewVideoCount> newVideoCountList = new ArrayList<>();
        for (int index = 0; index < videoInfoGroupList.size(); index++) {
            VideoInfoGroup videoInfoGroup = videoInfoGroupList.get(index);
            String time = videoInfoGroup.getTime();
            Integer number = videoInfoGroup.getNumber();

            count += number;
            NewVideoCount newVideoCount = new NewVideoCount().setTime(time).setNewVideoNum(count).setNewVideoAdd(number);
            newVideoCountList.add(newVideoCount);
        }
        return newVideoCountList.subList(newVideoCountList.size() - 14, newVideoCountList.size());
    }

}
