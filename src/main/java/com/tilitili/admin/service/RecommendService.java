package com.tilitili.admin.service;

import com.tilitili.admin.entity.RecommendFileItem;
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

    public List<RecommendFileItem> getRecommendFile(RecommendQuery query) {
        return list(query).stream().map(recommend -> {
            Long av = recommend.getAv();
            String operator = recommend.getOperator();
            String text = recommend.getText();
            Integer startTime = recommend.getStartTime();

            VideoInfo videoInfo = videoInfoMapper.getByAv(recommend.getAv());

            String name = videoInfo.getName();
            String owner = videoInfo.getOwner();
            String externalOwner = videoInfo.getExternalOwner();
            String type = videoInfo.getType();

            RecommendFileItem recommendFileItem = new RecommendFileItem();
            recommendFileItem.setAvStr(av.toString());
            recommendFileItem.setNameStr(name);
            recommendFileItem.setOperatorStr(operator);
            recommendFileItem.setTextStr(text);
            recommendFileItem.setStartTime(startTime);
            recommendFileItem.setOwnerStr(owner);
            recommendFileItem.setExternalOwnerStr(externalOwner);
            recommendFileItem.setTypeStr(type);
            return recommendFileItem;
        }).collect(Collectors.toList());
    }
}
