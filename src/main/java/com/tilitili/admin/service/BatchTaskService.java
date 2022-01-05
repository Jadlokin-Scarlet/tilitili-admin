package com.tilitili.admin.service;

import com.tilitili.admin.entity.view.BatchTaskView;
import com.tilitili.common.emnus.TaskStatus;
import com.tilitili.common.entity.BatchTask;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.query.BatchTaskQuery;
import com.tilitili.common.entity.query.TaskQuery;
import com.tilitili.common.entity.query.VideoInfoQuery;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.mapper.tilitili.BatchTaskMapper;
import com.tilitili.common.mapper.tilitili.TaskMapper;
import com.tilitili.common.mapper.tilitili.TouhouAllMapper;
import com.tilitili.common.mapper.tilitili.VideoInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.tilitili.common.emnus.TaskReason.*;
import static com.tilitili.common.emnus.TaskStatus.*;
import static com.tilitili.common.emnus.TaskType.BatchSpiderVideo;

@Service
public class BatchTaskService {
    private final VideoInfoMapper videoInfoMapper;
    private final BatchTaskMapper batchTaskMapper;
    private final TaskMapper taskMapper;
    private final TaskManager taskManager;
    private final TouhouAllMapper touhouAllMapper;

    @Autowired
    public BatchTaskService(VideoInfoMapper videoInfoMapper, BatchTaskMapper batchTaskMapper, TaskMapper taskMapper, TaskManager taskManager, TouhouAllMapper touhouAllMapper) {
        this.videoInfoMapper = videoInfoMapper;
        this.batchTaskMapper = batchTaskMapper;
        this.taskMapper = taskMapper;
        this.taskManager = taskManager;
        this.touhouAllMapper = touhouAllMapper;
    }

    public List<BatchTaskView> list(BatchTaskQuery batchTaskQuery) {
        return batchTaskMapper.getBatchTaskByCondition(batchTaskQuery).parallelStream().map(batchTask -> {
            Map<Integer, Integer> taskCountMap = taskManager.countByGroupStatus(new TaskQuery().setBatchId(batchTask.getId()));

            long successTaskNumber = taskCountMap.getOrDefault(SUCCESS.value, 0);
            long failTaskNumber = taskCountMap.getOrDefault(FAIL.value, 0) + taskCountMap.getOrDefault(TaskStatus.TIMEOUT.value, 0);
            long waitTaskNumber = taskCountMap.getOrDefault(WAIT.value, 0);
            Integer totalTaskNumber = taskMapper.countTaskByCondition(new TaskQuery().setBatchId(batchTask.getId()));

            BatchTaskView result = new BatchTaskView();
            result.setId(batchTask.getId());
            result.setStatus(batchTask.getStatus());
            result.setType(batchTask.getType());
            result.setCreateTime(batchTask.getCreateTime());
            result.setUpdateTime(batchTask.getUpdateTime());
            result.setRemark(batchTask.getRemark());
            result.setReason(batchTask.getReason());

            result.setSuccessTaskNumber(Math.toIntExact(successTaskNumber));
            result.setFailTaskNumber(Math.toIntExact(failTaskNumber));
            result.setWaitTaskNumber(Math.toIntExact(waitTaskNumber));
            result.setTotalTaskNumber(totalTaskNumber);
            return result;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void deleteBatchTask(Long batchId) {
        batchTaskMapper.delete(batchId);
        taskMapper.deleteByBatchId(batchId);
    }
}
