package com.tilitili.admin.sender;

import com.google.gson.GsonBuilder;
import com.tilitili.common.entity.message.TaskMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskSender {

    private final JmsTemplate jmsTemplate;

    private final String DESTINATION = "SpiderVideoTaskMessage";

    @Autowired
    public TaskSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendSpiderVideo(TaskMessage taskMessage) {
        jmsTemplate.convertAndSend(DESTINATION, taskMessage);
    }
}
