package com.tilitili.admin.sender;

import com.google.common.collect.ImmutableMap;
import com.tilitili.common.emnus.TaskType;
import com.tilitili.common.entity.message.TaskMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import java.util.Map;

@Slf4j
@Component
public class SpiderVideoViewTaskSender {

    private final Map<Integer, JmsTemplate> map;

    @Autowired
    public SpiderVideoViewTaskSender(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        String DESTINATION = "SpiderVideoViewTaskMessage";

        JmsTemplate jmsTemplate1 = newJmsTemplate(connectionFactory, messageConverter, DESTINATION, 1);
        JmsTemplate jmsTemplate4 = newJmsTemplate(connectionFactory, messageConverter, DESTINATION, 4);
        JmsTemplate jmsTemplate9 = newJmsTemplate(connectionFactory, messageConverter, DESTINATION, 9);

        map = ImmutableMap.<Integer, JmsTemplate>builder()
                .put(TaskType.SpiderVideo.getValue(), jmsTemplate9)
                .put(TaskType.BatchSpiderVideo.getValue(), jmsTemplate4)
                .put(TaskType.AutoBatchSpiderVideo.getValue(), jmsTemplate1)
                .build();
    }

    public void sendSpiderVideo(TaskMessage taskMessage) {
        JmsTemplate jmsTemplate = map.get(taskMessage.getType());
        if (jmsTemplate == null) {
            log.error("找不到JmsTemplate");
            return;
        }
        log.info("send task to mq{}{}: {}", jmsTemplate.getDefaultDestinationName(), jmsTemplate.getPriority(), taskMessage);
        jmsTemplate.convertAndSend(taskMessage);
    }

    private JmsTemplate newJmsTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter, String destination, Integer priority) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        jmsTemplate.setDefaultDestinationName(destination);
        jmsTemplate.setPriority(priority);
        jmsTemplate.setTimeToLive(7200000);
        jmsTemplate.setExplicitQosEnabled(true);
        return jmsTemplate;
    }

}
