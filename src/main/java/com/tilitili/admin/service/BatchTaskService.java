package com.tilitili.admin.service;

import com.tilitili.admin.entity.view.BatchTaskView;
import com.tilitili.common.emnus.TaskStatus;
import com.tilitili.common.entity.query.BatchTaskQuery;
import com.tilitili.common.entity.query.TaskQuery;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.mapper.rank.BatchTaskMapper;
import com.tilitili.common.mapper.rank.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tilitili.common.emnus.TaskStatus.*;

@Service
public class BatchTaskService {
    private final BatchTaskMapper batchTaskMapper;
    private final TaskMapper taskMapper;
    private final TaskManager taskManager;

    @Autowired
    public BatchTaskService(BatchTaskMapper batchTaskMapper, TaskMapper taskMapper, TaskManager taskManager) {
        this.batchTaskMapper = batchTaskMapper;
        this.taskMapper = taskMapper;
        this.taskManager = taskManager;
    }

    public List<BatchTaskView> list(BatchTaskQuery batchTaskQuery) {
        return batchTaskMapper.getBatchTaskByCondition(batchTaskQuery).parallelStream().map(batchTask -> {
            Map<Integer, Long> taskCountMap = taskManager.countByGroupStatus(new TaskQuery().setBatchId(batchTask.getId()));

            long successTaskNumber = taskCountMap.getOrDefault(SUCCESS.value, 0L);
            long failTaskNumber = taskCountMap.getOrDefault(FAIL.value, 0L) + taskCountMap.getOrDefault(TaskStatus.TIMEOUT.value, 0L);
            long waitTaskNumber = taskCountMap.getOrDefault(WAIT.value, 0L);
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

    @Transactional(transactionManager = "rankTransactionManager")
    public void deleteBatchTask(Long batchId) {
        batchTaskMapper.delete(batchId);
        taskMapper.deleteByBatchId(batchId);
    }
}
