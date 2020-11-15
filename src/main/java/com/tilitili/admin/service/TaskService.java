package com.tilitili.admin.service;

import com.tilitili.admin.sender.SpiderVideoViewTaskSender;
import com.tilitili.common.emnus.TaskType;
import com.tilitili.common.entity.BatchTask;
import com.tilitili.common.entity.Task;
import com.tilitili.common.entity.message.TaskMessage;
import com.tilitili.common.entity.view.SimpleTaskView;
import com.tilitili.common.mapper.BatchTaskMapper;
import com.tilitili.common.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class TaskService {
    private final TaskMapper taskMapper;
    private final BatchTaskMapper batchTaskMapper;
    private final SpiderVideoViewTaskSender spiderVideoViewTaskSender;

    @Autowired
    public TaskService(TaskMapper taskMapper, BatchTaskMapper batchTaskMapper, SpiderVideoViewTaskSender spiderVideoViewTaskSender) {
        this.taskMapper = taskMapper;
        this.batchTaskMapper = batchTaskMapper;
        this.spiderVideoViewTaskSender = spiderVideoViewTaskSender;
    }

    public void simpleSpiderVideo(SimpleTaskView simpleTaskView) {
        Long av = simpleTaskView.getAv();
        BatchTask batchTask = new BatchTask().setType(TaskType.SpiderVideo.getValue()).setReason(simpleTaskView.getReason());
        batchSpiderVideo(batchTask, Collections.singletonList(av));
    }

    public void batchSpiderVideo(BatchTask batchTask, List<Long> avList) {
        batchTaskMapper.insert(batchTask);
        avList.forEach(av -> {
            Task task = new Task().setAv(av).setBatchId(batchTask.getId()).setType(batchTask.getType());
            taskMapper.insert(task);

            TaskMessage taskMessage = new TaskMessage().setAv(av).setId(task.getId()).setType(batchTask.getType());
            spiderVideoViewTaskSender.sendSpiderVideo(taskMessage);
        });
    }

}
