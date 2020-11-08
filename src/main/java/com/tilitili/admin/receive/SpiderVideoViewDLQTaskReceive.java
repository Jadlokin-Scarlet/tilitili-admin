package com.tilitili.admin.receive;

import com.tilitili.common.emnus.TaskStatus;
import com.tilitili.common.entity.message.TaskMessage;
import com.tilitili.common.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SpiderVideoViewDLQTaskReceive {

    private final TaskMapper taskMapper;

    @Autowired
    public SpiderVideoViewDLQTaskReceive(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @JmsListener(destination = "DLQ.SpiderVideoViewTaskMessage")
    public void receiveSpiderVideoViewDLQTask(TaskMessage taskMessage) {
        taskMapper.updateStatusById(taskMessage.getId(), TaskStatus.WAIT.getValue(), TaskStatus.TIMEOUT.getValue());
    }

}
