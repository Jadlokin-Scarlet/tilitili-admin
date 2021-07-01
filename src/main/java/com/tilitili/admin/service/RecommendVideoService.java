package com.tilitili.admin.service;

import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.entity.query.RecommendTalkQuery;
import com.tilitili.common.entity.query.RecommendVideoQuery;
import com.tilitili.common.entity.resource.RecommendVideoIssueResource;
import com.tilitili.common.entity.resource.Resource;
import com.tilitili.common.mapper.RecommendMapper;
import com.tilitili.common.mapper.RecommendTalkMapper;
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
    private final RecommendTalkMapper recommendTalkMapper;

    @Autowired
    public RecommendVideoService(RecommendVideoMapper recommendVideoMapper, RecommendMapper recommendMapper, RecommendTalkMapper recommendTalkMapper) {
        this.recommendVideoMapper = recommendVideoMapper;
        this.recommendMapper = recommendMapper;
        this.recommendTalkMapper = recommendTalkMapper;
    }

    public List<RecommendVideo> list(RecommendVideoQuery query) {
        return recommendVideoMapper.list(query).stream().peek(recommendVideo -> {
            Integer issueId = recommendVideo.getId();
            List<Recommend> recommendList = recommendMapper.list(new RecommendQuery().setIssueId(issueId).setType(0).setStatus(1).setSorter("sort_num", "desc"));
            recommendVideo.setRecommendList(recommendList);
            recommendVideo.setRecommendNumber(recommendList.size());
            List<Recommend> selfRecommendList = recommendMapper.list(new RecommendQuery().setIssueId(issueId).setType(1).setStatus(1).setSorter("sort_num", "desc"));
            recommendVideo.setSelfRecommendList(selfRecommendList);
            recommendVideo.setSelfRecommendNumber(selfRecommendList.size());
            boolean hasTalk = ! recommendTalkMapper.list(new RecommendTalkQuery().setIssueId(issueId).setStatus(0).setPageSize(1)).isEmpty();
            recommendVideo.setHasTalk(hasTalk);
        }).collect(Collectors.toList());
    }

    public List<Resource> listIssue() {
        List<RecommendVideo> recommendVideoList = recommendVideoMapper.list(new RecommendVideoQuery().setStatus(0).setPageSize(1000));
        return recommendVideoList.stream().filter(Objects::nonNull).map(RecommendVideoIssueResource::new).collect(Collectors.toList());
    }
}
