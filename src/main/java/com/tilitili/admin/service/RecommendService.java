package com.tilitili.admin.service;

import com.tilitili.admin.entity.RecommendFileItem;
import com.tilitili.common.entity.Admin;
import com.tilitili.common.entity.Owner;
import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.dto.RecommendDTO;
import com.tilitili.common.entity.query.OwnerQuery;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.manager.RecommendManager;
import com.tilitili.common.mapper.rank.*;
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
    public RecommendService(RecommendMapper recommendMapper, RecommendVideoMapper recommendVideoMapper, RecommendManager recommendManager, OwnerMapper ownerMapper, AdminMapper adminMapper) {
        this.recommendMapper = recommendMapper;
        this.recommendVideoMapper = recommendVideoMapper;
        this.recommendManager = recommendManager;
        this.ownerMapper = ownerMapper;
        this.adminMapper = adminMapper;
    }

    public List<Recommend> list(RecommendQuery query) {
        return recommendMapper.list(query).parallelStream().map(recommend -> {
            if (recommend.getIssueId() != null) {
                RecommendVideo recommendVideo = recommendVideoMapper.getById(recommend.getIssueId());
                recommend.setIssueName(recommendVideo.getName());
                recommend.setIssue(recommendVideo.getIssue());
            }
            return recommend;
        }).collect(Collectors.toList());
    }

    public void supplementRecommend(List<RecommendDTO> recommendList) {
        for (RecommendDTO recommend : recommendList) {
            if (recommend.getIssueId() != null) {
                RecommendVideo recommendVideo = recommendVideoMapper.getById(recommend.getIssueId());
                recommend.setIssueName(recommendVideo.getName());
                recommend.setIssue(recommendVideo.getIssue());
            }
        }
    }

    public List<RecommendFileItem> getRecommendFile(RecommendQuery query) {
        return recommendManager.listUseRecommend(query).stream().map(recommend -> {
            Long av = recommend.getAv();
            String operator = recommend.getOperator();
            Integer startTime = recommend.getStartTime();
            Integer endTime = recommend.getEndTime();

            String name = recommend.getName();
            String owner = recommend.getOwner();
            String externalOwner = recommend.getExternalOwner();
            String videoType = recommend.getVideoType();
            String pubTime = recommend.getPubTime();

            Admin admin = adminMapper.getNormalAdminByName(operator);

            String face = null;
            if (admin != null) {
                face = admin.getFace();
            }

            RecommendFileItem recommendFileItem = new RecommendFileItem();
            recommendFileItem.setAv(av);
            recommendFileItem.setOperator(operator);
            recommendFileItem.setStartTime(startTime);
            recommendFileItem.setEndTime(endTime);

            recommendFileItem.setAvStr(av.toString());
            recommendFileItem.setNameStr(name);
            recommendFileItem.setOperatorStr(operator);
            recommendFileItem.setOwnerStr(owner);
            recommendFileItem.setExternalOwnerStr(externalOwner);
            recommendFileItem.setTypeStr(videoType);
            recommendFileItem.setFace(face);
            if (pubTime != null) {
                recommendFileItem.setPubTimeStr(pubTime.split(" ")[0]);
            }
            return recommendFileItem;
        }).collect(Collectors.toList());
    }

    public List<RecommendFileItem> getSelfRecommendFile(RecommendQuery query) {
        return recommendManager.listSelfRecommend(query).stream().map(recommend -> {
            Long av = recommend.getAv();
            Integer startTime = recommend.getStartTime();
            Integer endTime = recommend.getEndTime();

            String name = recommend.getName();
            String ownerName = recommend.getOwner();
            String externalOwner = recommend.getExternalOwner();
            String videoType = recommend.getVideoType();
            String pubTime = recommend.getPubTime();

            List<Owner> ownerList = ownerMapper.getOwnerByCondition(new OwnerQuery().setName(ownerName));

            String face = null;
            if (ownerList.size() == 1) {
                face = ownerList.get(0).getFace();
            }

            RecommendFileItem recommendFileItem = new RecommendFileItem();
            recommendFileItem.setAv(av);
            recommendFileItem.setOperator(ownerName);
            recommendFileItem.setStartTime(startTime);
            recommendFileItem.setEndTime(endTime);

            recommendFileItem.setAvStr(av.toString());
            recommendFileItem.setNameStr(name);
            recommendFileItem.setOperatorStr(ownerName);
            recommendFileItem.setOwnerStr(ownerName);
            recommendFileItem.setExternalOwnerStr(externalOwner);
            recommendFileItem.setTypeStr(videoType);
            recommendFileItem.setFace(face);
            if (pubTime != null) {
                recommendFileItem.setPubTimeStr(pubTime.split(" ")[0]);
            }
            return recommendFileItem;
        }).collect(Collectors.toList());
    }

    public void unUseRecommend(Long id) {
        Recommend recommend = recommendMapper.getNormalRecommendById(id);
        if (recommend == null || recommend.getStatus() == -1) {
            return;
        }
        recommendMapper.unUseRecommend(id);
    }
}
