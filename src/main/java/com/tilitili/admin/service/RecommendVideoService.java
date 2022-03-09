package com.tilitili.admin.service;

import com.tilitili.admin.entity.view.RecommendVideoView;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.dto.RecommendDTO;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.entity.query.RecommendTalkQuery;
import com.tilitili.common.entity.query.RecommendVideoQuery;
import com.tilitili.common.entity.view.resource.RecommendVideoIssueResource;
import com.tilitili.common.entity.view.resource.Resource;
import com.tilitili.common.mapper.rank.RecommendMapper;
import com.tilitili.common.mapper.rank.RecommendTalkMapper;
import com.tilitili.common.mapper.rank.RecommendVideoMapper;
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
        return recommendVideoMapper.getRecommendVideoByCondition(query).stream().map(recommendVideo -> {
            Integer issueId = recommendVideo.getId();
            List<RecommendDTO> recommendList = recommendMapper.list(new RecommendQuery().setIssueId(issueId).setType(0).setStatus(1).setSorter("sort_num").setSorted("desc").setSubSorter("id").setSubSorted("asc"));
            List<RecommendDTO> selfRecommendList = recommendMapper.list(new RecommendQuery().setIssueId(issueId).setType(1).setStatus(1).setSorter("sort_num").setSorted("desc").setSubSorter("id").setSubSorted("asc"));
            boolean hasTalk = ! recommendTalkMapper.getRecommendTalkByCondition(new RecommendTalkQuery().setIssueId(issueId).setStatus(0).setPageSize(1)).isEmpty();

            RecommendVideoView result = new RecommendVideoView();
            result.setId(issueId);
            result.setName(recommendVideo.getName());
            result.setCreateTime(recommendVideo.getCreateTime());
            result.setUpdateTime(recommendVideo.getUpdateTime());
            result.setType(recommendVideo.getType());
            result.setStatus(recommendVideo.getStatus());
            result.setIssue(recommendVideo.getIssue());

            result.setRecommendList(recommendList);
            result.setRecommendNumber(recommendList.size());
            result.setSelfRecommendList(selfRecommendList);
            result.setSelfRecommendNumber(selfRecommendList.size());
            result.setHasTalk(hasTalk);
            return result;
        }).collect(Collectors.toList());
    }

    public List<Resource> listIssue() {
        List<RecommendVideo> recommendVideoList = recommendVideoMapper.getRecommendVideoByCondition(new RecommendVideoQuery().setStatus(0).setPageSize(1000).setSorter("id").setSorted("desc"));
        return recommendVideoList.stream().filter(Objects::nonNull).map(RecommendVideoIssueResource::new).collect(Collectors.toList());
    }
}
