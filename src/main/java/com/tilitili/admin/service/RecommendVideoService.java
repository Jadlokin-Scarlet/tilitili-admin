package com.tilitili.admin.service;

import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.entity.query.RecommendVideoQuery;
import com.tilitili.common.entity.resource.RecommendVideoIssueResource;
import com.tilitili.common.entity.resource.Resource;
import com.tilitili.common.mapper.RecommendMapper;
import com.tilitili.common.mapper.RecommendVideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RecommendVideoService {
    private final RecommendVideoMapper recommendVideoMapper;
    private final RecommendMapper recommendMapper;

    @Autowired
    public RecommendVideoService(RecommendVideoMapper recommendVideoMapper, RecommendMapper recommendMapper) {
        this.recommendVideoMapper = recommendVideoMapper;
        this.recommendMapper = recommendMapper;
    }

    public List<RecommendVideo> list(RecommendVideoQuery query) {
        return recommendVideoMapper.list(query).stream().peek(recommendVideo -> {
            Integer issueId = recommendVideo.getId();
            int recommendNumber = recommendMapper.count(new RecommendQuery().setIssueId(issueId).setType(0));
            recommendVideo.setRecommendNumber(recommendNumber);
            int selfRecommendNumber = recommendMapper.count(new RecommendQuery().setIssueId(issueId).setType(1));
            recommendVideo.setSelfRecommendNumber(selfRecommendNumber);
        }).collect(Collectors.toList());
    }

    public List<Resource> listIssue() {
        List<RecommendVideo> recommendVideoList = recommendVideoMapper.list(new RecommendVideoQuery().setStatus(0).setPageSize(1000));
        return recommendVideoList.stream().filter(Objects::nonNull).map(RecommendVideoIssueResource::new).collect(Collectors.toList());
    }
}
