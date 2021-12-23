package com.tilitili.admin.service;

import com.tilitili.admin.entity.count.sub.VideoDataAddCount;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.dto.VideoDTO;
import com.tilitili.common.entity.dto.VideoDataGroup;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.resource.Resource;
import com.tilitili.common.manager.VideoDataManager;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class VideoDataService {

    private final Integer RANK_LIMIT = 1000;
    private final AtomicBoolean isRanking = new AtomicBoolean(false);

    private final VideoDataManager videoDataManager;

    @Autowired
    public VideoDataService(VideoDataManager videoDataManager) {
        this.videoDataManager = videoDataManager;
    }

    public List<Resource> getIssueResource() {
        return videoDataManager.listIssue().stream().map(Resource::new).collect(Collectors.toList());
    }

    public void reRank(int issue) {
        Asserts.isTrue(isRanking.compareAndSet(false, true), "正在重排，请稍后再试。");
        log.info("清理rank level");
        videoDataManager.clearRank(issue);
        log.info("清理完毕");
        List<VideoDTO> videoDataList = videoDataManager.listForReRank(issue);
        log.info("size={}", videoDataList.size());
        int rankWithoutLen = 1;
        for (int index = 0; index < videoDataList.size(); index++) {
            VideoDTO videoDTO = videoDataList.get(index);
            long rank = index + 1;
            Long av = videoDTO.getAv();
            Long oldRank = videoDTO.getRank();
            Integer hisRank = videoDTO.getHisRank();
            Integer moreHisRank = videoDTO.getMoreHisRank();

            VideoData upd = new VideoData();
            upd.setAv(av);
            upd.setIssue(issue);
            upd.setRank(rank);

            if (rank < 150) {
                boolean isLen = rank > 0 && hisRank > 0 && moreHisRank > 0 && rank <= 30 && hisRank <= 30 && moreHisRank <= 30;

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
            }


            log.info("av{} issue={} oldRank={} rank={} level={}", av, issue, oldRank, rank, upd.getLevel());
            videoDataManager.updateVideoDataSelective(upd);
        }
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
