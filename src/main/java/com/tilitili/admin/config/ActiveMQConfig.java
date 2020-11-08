package com.tilitili.admin.config;

import org.apache.activemq.RedeliveryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class ActiveMQConfig {

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("Message");
        return converter;
    }

//    @Bean
//    public RedeliveryPolicy redeliveryPolicy() {
//        RedeliveryPolicy queuePolicy = new RedeliveryPolicy();
//        queuePolicy.setInitialRedeliveryDelay(0);
//        queuePolicy.setRedeliveryDelay(1000);
//        queuePolicy.setUseExponentialBackOff(false);
//        queuePolicy.setMaximumRedeliveries(2);
//        return queuePolicy;
//    }

}
