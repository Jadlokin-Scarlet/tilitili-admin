package com.tilitili.admin.service;

import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.Resource;
import com.tilitili.common.entity.Task;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.entity.query.TaskQuery;
import com.tilitili.common.mapper.RecommendMapper;
import com.tilitili.common.mapper.TaskMapper;
import com.tilitili.common.mapper.VideoInfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RecommendService {
    private final RecommendMapper recommendMapper;
    private final VideoInfoMapper videoInfoMapper;
    private final TaskMapper taskMapper;

    @Autowired
    public RecommendService(RecommendMapper recommendMapper, VideoInfoMapper videoInfoMapper, TaskMapper taskMapper) {
        this.recommendMapper = recommendMapper;
        this.videoInfoMapper = videoInfoMapper;
        this.taskMapper = taskMapper;
    }

    public List<Recommend> list(RecommendQuery query) {
        return recommendMapper.list(query).parallelStream().peek(recommend -> {
            VideoInfo videoInfo = videoInfoMapper.getByAv(recommend.getAv());
            if (videoInfo != null) {
                BeanUtils.copyProperties(videoInfo, recommend);
                if (videoInfo.getStatus() != 0) {
                    List<Task> taskList = taskMapper.list(new TaskQuery().setValue(videoInfo.getAv().toString()));
                    Task task = taskList.get(taskList.size() - 1);
                    recommend.setName(task.getRemark());
                }
            }
        }).collect(Collectors.toList());
    }

    public List<Resource> listIssue() {
        List<Integer> recommendIssueList = recommendMapper.listRecommendIssue();
        Collections.reverse(recommendIssueList);
        return recommendIssueList.stream().filter(Objects::nonNull).map(Resource::new).collect(Collectors.toList());
    }
}
