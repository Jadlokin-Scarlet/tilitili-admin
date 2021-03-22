package com.tilitili.admin.service;

import com.tilitili.admin.entity.RecommendFileItem;
import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.manager.RecommendManager;
import com.tilitili.common.mapper.RecommendMapper;
import com.tilitili.common.mapper.RecommendVideoMapper;
import com.tilitili.common.mapper.TaskMapper;
import com.tilitili.common.mapper.VideoInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendService {
    private final RecommendMapper recommendMapper;
    private final RecommendVideoMapper recommendVideoMapper;
    private final RecommendManager recommendManager;

    @Autowired
    public RecommendService(RecommendMapper recommendMapper, VideoInfoMapper videoInfoMapper, TaskMapper taskMapper, RecommendVideoMapper recommendVideoMapper, RecommendManager recommendManager) {
        this.recommendMapper = recommendMapper;
        this.recommendVideoMapper = recommendVideoMapper;
        this.recommendManager = recommendManager;
    }

    public List<Recommend> list(RecommendQuery query) {
        return recommendMapper.list(query).parallelStream().peek(recommend -> {
            if (recommend.getIssueId() != null) {
                RecommendVideo recommendVideo = recommendVideoMapper.getById(recommend.getIssueId());
                recommend.setIssueName(recommendVideo.getName());
                recommend.setIssue(recommendVideo.getIssue());
            }
        }).collect(Collectors.toList());
    }

    public void supplementRecommend(List<Recommend> recommendList) {
        for (Recommend recommend : recommendList) {
            if (recommend.getIssueId() != null) {
                RecommendVideo recommendVideo = recommendVideoMapper.getById(recommend.getIssueId());
                recommend.setIssueName(recommendVideo.getName());
                recommend.setIssue(recommendVideo.getIssue());
            }
        }
    }

    public List<RecommendFileItem> getRecommendFile(RecommendQuery query) {
        return recommendManager.listRecommend(query).stream().map(recommend -> {
            Long av = recommend.getAv();
            String operator = recommend.getOperator();
            String text = recommend.getText();
            Integer startTime = recommend.getStartTime();
            Integer endTime = recommend.getEndTime();

            String name = recommend.getName();
            String owner = recommend.getOwner();
            String externalOwner = recommend.getExternalOwner();
            String videoType = recommend.getVideoType();
            String pubTime = recommend.getPubTime();

            RecommendFileItem recommendFileItem = new RecommendFileItem();
            recommendFileItem.setAv(av);
            recommendFileItem.setStartTime(startTime);
            recommendFileItem.setEndTime(endTime);

            recommendFileItem.setAvStr(av.toString());
            recommendFileItem.setNameStr(name);
            recommendFileItem.setOperatorStr(operator);
            recommendFileItem.setTextStr(text);
            recommendFileItem.setOwnerStr(owner);
            recommendFileItem.setExternalOwnerStr(externalOwner);
            recommendFileItem.setTypeStr(videoType);
            if (pubTime != null) {
                recommendFileItem.setPubTimeStr(pubTime.split(" ")[0]);
            }
            return recommendFileItem;
        }).collect(Collectors.toList());
    }

    public List<RecommendFileItem> getSelfRecommendFile(RecommendQuery query) {
        return recommendManager.listSelfRecommend(query).stream().map(recommend -> {
            Long av = recommend.getAv();
            String operator = recommend.getOperator();
            String text = recommend.getText();
            Integer startTime = recommend.getStartTime();
            Integer endTime = recommend.getEndTime();

            String name = recommend.getName();
            String owner = recommend.getOwner();
            String externalOwner = recommend.getExternalOwner();
            String videoType = recommend.getVideoType();
            String pubTime = recommend.getPubTime();

            RecommendFileItem recommendFileItem = new RecommendFileItem();
            recommendFileItem.setAv(av);
            recommendFileItem.setStartTime(startTime);
            recommendFileItem.setEndTime(endTime);

            recommendFileItem.setAvStr(av.toString());
            recommendFileItem.setNameStr(name);
            recommendFileItem.setOperatorStr("");
            recommendFileItem.setTextStr(text);
            recommendFileItem.setOwnerStr(owner);
            recommendFileItem.setExternalOwnerStr(externalOwner);
            recommendFileItem.setTypeStr(videoType);
            if (pubTime != null) {
                recommendFileItem.setPubTimeStr(pubTime.split(" ")[0]);
            }
            return recommendFileItem;
        }).collect(Collectors.toList());
    }

    public void unUseRecommend(Long id) {
        Recommend recommend = recommendMapper.getById(id);
        if (recommend == null || recommend.getStatus() == -1) {
            return;
        }

        Recommend updateRecommend = new Recommend();
        updateRecommend.setId(id);
        updateRecommend.setStatus(0);
        updateRecommend.setIssueId(-1);
        recommendMapper.update(updateRecommend);
    }
}
