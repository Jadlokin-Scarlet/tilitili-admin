package com.tilitili.admin.service;

import com.tilitili.admin.entity.RecommendFileItem;
import com.tilitili.common.entity.Admin;
import com.tilitili.common.entity.Owner;
import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.query.OwnerQuery;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.manager.RecommendManager;
import com.tilitili.common.mapper.*;
import com.tilitili.common.utils.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendService {
    private final RecommendMapper recommendMapper;
    private final RecommendVideoMapper recommendVideoMapper;
    private final RecommendManager recommendManager;
    private final OwnerMapper ownerMapper;
    private final AdminMapper adminMapper;

    @Autowired
    public RecommendService(RecommendMapper recommendMapper, VideoInfoMapper videoInfoMapper, TaskMapper taskMapper, RecommendVideoMapper recommendVideoMapper, RecommendManager recommendManager, OwnerMapper ownerMapper, AdminMapper adminMapper) {
        this.recommendMapper = recommendMapper;
        this.recommendVideoMapper = recommendVideoMapper;
        this.recommendManager = recommendManager;
        this.ownerMapper = ownerMapper;
        this.adminMapper = adminMapper;
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

            Admin admin = adminMapper.getByName(operator);

            String textStr = text.replaceAll("\n", " ");

            RecommendFileItem recommendFileItem = new RecommendFileItem();
            recommendFileItem.setAv(av);
            recommendFileItem.setStartTime(startTime);
            recommendFileItem.setEndTime(endTime);

            recommendFileItem.setAvStr(av.toString());
            recommendFileItem.setNameStr(name);
            recommendFileItem.setOperatorStr(operator);
            recommendFileItem.setTextStr(textStr);
            recommendFileItem.setOwnerStr(owner);
            recommendFileItem.setExternalOwnerStr(externalOwner);
            recommendFileItem.setTypeStr(videoType);
            recommendFileItem.setFace(admin.getFace());
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
            String ownerName = recommend.getOwner();
            String externalOwner = recommend.getExternalOwner();
            String videoType = recommend.getVideoType();
            String pubTime = recommend.getPubTime();

            List<Owner> ownerList = ownerMapper.list(new OwnerQuery().setName(ownerName));
            Asserts.isTrue(ownerList.size() > 0, "作者没找到");
            Asserts.isTrue(ownerList.size() < 2, "重名?");
            Owner owner = ownerList.get(0);

            String textStr = text.replaceAll("\n", " ");

            RecommendFileItem recommendFileItem = new RecommendFileItem();
            recommendFileItem.setAv(av);
            recommendFileItem.setStartTime(startTime);
            recommendFileItem.setEndTime(endTime);

            recommendFileItem.setAvStr(av.toString());
            recommendFileItem.setNameStr(name);
            recommendFileItem.setOperatorStr(ownerName);
            recommendFileItem.setTextStr(textStr);
            recommendFileItem.setOwnerStr(ownerName);
            recommendFileItem.setExternalOwnerStr(externalOwner);
            recommendFileItem.setTypeStr(videoType);
            recommendFileItem.setFace(owner.getFace());
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
