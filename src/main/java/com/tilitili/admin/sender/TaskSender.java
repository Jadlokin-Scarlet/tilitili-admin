package com.tilitili.admin.sender;

import com.tilitili.admin.entity.message.TaskMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskSender {

    private final JmsTemplate jmsTemplate;

    private final String DESTINATION = "SpiderVideoTask";

    @Autowired
    public TaskSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendSpiderVideo(TaskMessage taskMessage) {
        jmsTemplate.convertAndSend(DESTINATION, taskMessage);
    }
}
