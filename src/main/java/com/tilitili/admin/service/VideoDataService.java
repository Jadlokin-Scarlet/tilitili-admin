package com.tilitili.admin.service;

import com.tilitili.admin.entity.count.sub.VideoDataAddCount;
import com.tilitili.common.entity.dto.VideoDataGroup;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.resource.Resource;
import com.tilitili.common.manager.VideoDataManager;
import com.tilitili.common.mapper.tilitili.VideoDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class VideoDataService {
    public static final Integer RANK_LIMIT = 1000;

    private final VideoDataMapper videoDataMapper;
    private final VideoDataManager videoDataManager;

    @Autowired
    public VideoDataService(VideoDataMapper videoDataMapper, VideoDataManager videoDataManager) {
        this.videoDataMapper = videoDataMapper;
        this.videoDataManager = videoDataManager;
    }

    public List<Resource> getIssueResource() {
        Integer newIssue = videoDataMapper.getNewIssue();
        return IntStream.range(-38, newIssue).mapToObj(Resource::new).collect(Collectors.toList());
    }

    public void reRank(int issue) {
        log.info("清理rank level");
        videoDataMapper.clearRank(issue);
        log.info("清理完毕");
        videoDataMapper.reRankByIssue(issue, RANK_LIMIT);
        log.info("更新rank level 完毕");
    }

    public List<VideoDataAddCount> getVideoDataCount(VideoDataQuery query) {
        List<VideoDataGroup> groupList = videoDataManager.groupByIssue(query.setPageSize(10));
        Collections.reverse(groupList);
        List<VideoDataAddCount> result = new ArrayList<>();
        for (int index = 1; index < groupList.size(); index++) {
            VideoDataGroup videoDataGroup = groupList.get(index);
            VideoDataGroup oldVideoDataGroup = groupList.get(index - 1);

            Integer issue = videoDataGroup.getIssue();
            Integer view = videoDataGroup.getView() / (oldVideoDataGroup.getView() / 10000) - 10000;
            Integer reply = videoDataGroup.getReply() / (oldVideoDataGroup.getReply() / 10000) - 10000;
            Integer favorite = videoDataGroup.getFavorite() / (oldVideoDataGroup.getFavorite() / 10000) - 10000;
            Integer coin = videoDataGroup.getCoin() / (oldVideoDataGroup.getCoin() / 10000) - 10000;

            result.add(new VideoDataAddCount().setIssue(issue).setType("view").setNumber(view));
            result.add(new VideoDataAddCount().setIssue(issue).setType("reply").setNumber(reply));
            result.add(new VideoDataAddCount().setIssue(issue).setType("favorite").setNumber(favorite));
            result.add(new VideoDataAddCount().setIssue(issue).setType("coin").setNumber(coin));
        }
        return result;
    }
}
