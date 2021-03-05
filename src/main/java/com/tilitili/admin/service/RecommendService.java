package com.tilitili.admin.service;

import com.tilitili.common.entity.Recommend;
import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.Task;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.query.RecommendQuery;
import com.tilitili.common.entity.query.TaskQuery;
import com.tilitili.common.mapper.RecommendMapper;
import com.tilitili.common.mapper.RecommendVideoMapper;
import com.tilitili.common.mapper.TaskMapper;
import com.tilitili.common.mapper.VideoInfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendService {
    private final RecommendMapper recommendMapper;
    private final VideoInfoMapper videoInfoMapper;
    private final TaskMapper taskMapper;
    private final RecommendVideoMapper recommendVideoMapper;

    @Autowired
    public RecommendService(RecommendMapper recommendMapper, VideoInfoMapper videoInfoMapper, TaskMapper taskMapper, RecommendVideoMapper recommendVideoMapper) {
        this.recommendMapper = recommendMapper;
        this.videoInfoMapper = videoInfoMapper;
        this.taskMapper = taskMapper;
        this.recommendVideoMapper = recommendVideoMapper;
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

            if (recommend.getIssueId() != null) {
                RecommendVideo recommendVideo = recommendVideoMapper.getById(recommend.getIssueId());
                recommend.setIssueName(recommendVideo.getName());
                recommend.setIssue(recommendVideo.getIssue());
            }
        }).collect(Collectors.toList());
    }
}
