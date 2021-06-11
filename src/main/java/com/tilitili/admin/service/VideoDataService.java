package com.tilitili.admin.service;

import com.tilitili.admin.entity.count.sub.VideoDataAddCount;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.dto.VideoDataGroup;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.resource.Resource;
import com.tilitili.common.manager.VideoDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        videoDataManager.clearRank(issue);
        List<VideoData> videoDataList = videoDataManager.list(new VideoDataQuery().setIssue(issue).setStatus(0).setIsDelete(false).setPageSize(RANK_LIMIT).setSorter("point", "desc"));

        int rankWithoutLen = 1;
        for (int index = 0; index < videoDataList.size(); index++) {
            VideoData videoData = videoDataList.get(index);
            int rank = index + 1;
            Long av = videoData.getAv();

            VideoData hisData = videoDataManager.getByAvAndIssue(av, issue - 1);
            VideoData moreHisData = videoDataManager.getByAvAndIssue(av, issue - 2);
            int hisRank = Optional.ofNullable(hisData).map(VideoData::getRank).orElse(0);
            int moreHisRank = Optional.ofNullable(moreHisData).map(VideoData::getRank).orElse(0);
            boolean isLen = rank > 0 && hisRank > 0 && moreHisRank > 0 && rank <= 30 && hisRank <= 30 && moreHisRank <=30;

            VideoData upd = new VideoData();
            upd.setAv(av);
            upd.setIssue(issue);
            upd.setRank(rank);
            if (rankWithoutLen < 4) {
                upd.setLevel(1);
            } else if (rankWithoutLen < 11) {
                upd.setLevel(2);
            } else if (rankWithoutLen < 21) {
                upd.setLevel(3);
            } else if (rankWithoutLen < 31) {
                upd.setLevel(4);
            } else if (rankWithoutLen < 101) {
                upd.setLevel(5);
            }

            if (! isLen) {
                rankWithoutLen ++;
            }

            log.info("av{} issue={} oldRank={} rank={} level={}", av, issue, videoData.getRank(), rank, upd.getLevel());
            videoDataManager.update(videoData);
        }
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

    public Boolean isRank(Integer issue) {
        List<VideoData> videoDataList = videoDataManager.list(new VideoDataQuery().setIssue(issue).setStatus(0).setIsDelete(false).setSorter("point", "desc").setPageSize(1).setCurrent(1));
        if (videoDataList.isEmpty()) {
            return false;
        }
        return videoDataList.get(0).getRank() != 0;
    }

}
