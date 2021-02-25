package com.tilitili.admin.service;

import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.Resource;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.mapper.RecommendMapper;
import com.tilitili.common.mapper.VideoInfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendService {
    private final RecommendMapper recommendMapper;
    private final VideoInfoMapper videoInfoMapper;

    @Autowired
    public RecommendService(RecommendMapper recommendMapper, VideoInfoMapper videoInfoMapper) {
        this.recommendMapper = recommendMapper;
        this.videoInfoMapper = videoInfoMapper;
    }

    public List<Recommend> list(RecommendQuery query) {
        return recommendMapper.list(query).parallelStream().peek(recommend -> {
            VideoInfo videoInfo = videoInfoMapper.getByAv(recommend.getAv());
            if (videoInfo != null) {
                BeanUtils.copyProperties(videoInfo, recommend);
            }
        }).collect(Collectors.toList());
    }

    public List<Resource> listIssue() {
        List<Integer> recommendIssueList = recommendMapper.listRecommendIssue();
        Collections.reverse(recommendIssueList);
        return recommendIssueList.stream().map(Resource::new).collect(Collectors.toList());
    }
}
