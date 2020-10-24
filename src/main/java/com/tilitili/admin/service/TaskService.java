package com.tilitili.admin.service;

import com.tilitili.admin.entity.Task;
import com.tilitili.admin.entity.message.TaskMessage;
import com.tilitili.admin.mapper.TaskMapper;
import com.tilitili.admin.sender.TaskSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final TaskMapper taskMapper;
    private final TaskSender taskSender;

    @Autowired
    public TaskService(TaskMapper taskMapper, TaskSender taskSender) {
        this.taskMapper = taskMapper;
        this.taskSender = taskSender;
    }

    public void spiderVideo(Long av) {
        Task task = new Task().setAv(av).setType(0);
        taskMapper.addTask(task);
        taskSender.sendSpiderVideo(new TaskMessage().setAv(av).setId(task.getId()));
    }
}
