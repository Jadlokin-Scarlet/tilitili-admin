package com.tilitili.admin.service;

import com.tilitili.admin.entity.count.sub.VideoDataAddCount;
import com.tilitili.common.entity.resource.Resource;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.dto.VideoDataGroup;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.manager.VideoDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class VideoDataService {

    private final Integer RANK_LIMIT = 1000;

    private final VideoDataManager videoDataManager;

    @Autowired
    public VideoDataService(VideoDataManager videoDataManager) {
        this.videoDataManager = videoDataManager;
    }

    public List<Resource> getIssueResource() {
        return videoDataManager.listIssue().stream()
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
        List<VideoData> videoList = videoDataManager.list(videoDataQuery);
        IntStream.range(0, RANK_LIMIT)//.parallel()
                .filter(index -> !videoList.get(index).getRank().equals(index + 1))
                .peek(index -> log.info("av{} rank{} update to {}", videoList.get(index).getAv(), videoList.get(index).getRank(), index + 1))
                .mapToObj(index -> videoList.get(index).setRank(index + 1))
                .forEach(videoDataManager::updateRank);
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
