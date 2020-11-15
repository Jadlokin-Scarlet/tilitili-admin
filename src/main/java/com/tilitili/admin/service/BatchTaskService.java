package com.tilitili.admin.service;

import com.tilitili.common.emnus.TaskReason;
import com.tilitili.common.emnus.TaskStatus;
import com.tilitili.common.emnus.TaskType;
import com.tilitili.common.entity.BatchTask;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.query.BatchTaskQuery;
import com.tilitili.common.entity.query.TaskQuery;
import com.tilitili.common.entity.query.VideoInfoQuery;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.mapper.BatchTaskMapper;
import com.tilitili.common.mapper.TaskMapper;
import com.tilitili.common.mapper.VideoInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BatchTaskService {
    private final VideoInfoMapper videoInfoMapper;
    private final BatchTaskMapper batchTaskMapper;
    private final TaskMapper taskMapper;
    private final TaskService taskService;
    private final TaskManager taskManager;

    @Autowired
    public BatchTaskService(VideoInfoMapper videoInfoMapper, BatchTaskMapper batchTaskMapper, TaskMapper taskMapper, TaskService taskService, TaskManager taskManager) {
        this.videoInfoMapper = videoInfoMapper;
        this.batchTaskMapper = batchTaskMapper;
        this.taskMapper = taskMapper;
        this.taskService = taskService;
        this.taskManager = taskManager;
    }

    public List<BatchTask> list(BatchTaskQuery batchTaskQuery) {
        return batchTaskMapper.list(batchTaskQuery).stream().parallel().peek(batchTask -> {
            Map<Integer, Integer> taskCountMap = taskManager.countByGroupStatus(new TaskQuery().setBatchId(batchTask.getId()));

            long successTaskNumber = taskCountMap.getOrDefault(TaskStatus.SUCCESS.getValue(), 0);
            long failTaskNumber = taskCountMap.getOrDefault(TaskStatus.FAIL.getValue(), 0) + taskCountMap.getOrDefault(TaskStatus.TIMEOUT.getValue(), 0);
            long waitTaskNumber = taskCountMap.getOrDefault(TaskStatus.WAIT.getValue(), 0);
            Integer totalTaskNumber = taskMapper.count(new TaskQuery().setBatchId(batchTask.getId()));

            batchTask.setSuccessTaskNumber(Math.toIntExact(successTaskNumber));
            batchTask.setFailTaskNumber(Math.toIntExact(failTaskNumber));
            batchTask.setWaitTaskNumber(Math.toIntExact(waitTaskNumber));
            batchTask.setTotalTaskNumber(totalTaskNumber);
        }).collect(Collectors.toList());
    }

    public void testBatchSpiderVideo() {
        BatchTask batchTask = new BatchTask().setType(TaskType.BatchSpiderVideo.getValue()).setReason(TaskReason.NO_REASON.getValue());
        List<Long> avList = videoInfoMapper.list(new VideoInfoQuery().setAv(12L)).stream().map(VideoInfo::getAv).collect(Collectors.toList());
        taskService.batchSpiderVideo(batchTask, avList);
    }

    public void batchSpiderHiddenVideo() {
        BatchTask batchTask = new BatchTask().setType(TaskType.BatchSpiderVideo.getValue()).setReason(TaskReason.RE_SPIDER_HIDDEN_VIDEO.getValue());
        List<Long> avList = videoInfoMapper.listHiddenVideo().stream().map(VideoInfo::getAv).collect(Collectors.toList());
        taskService.batchSpiderVideo(batchTask, avList);
    }

}
