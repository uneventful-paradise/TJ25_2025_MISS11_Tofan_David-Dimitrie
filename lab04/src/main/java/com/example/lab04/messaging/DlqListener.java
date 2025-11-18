package com.example.lab04.messaging;

import com.example.lab04.config.RabbitMQConfig;
import com.example.lab04.dto.GradeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DlqListener {
    private static final Logger log = LoggerFactory.getLogger(DlqListener.class);

    @RabbitListener(queues = RabbitMQConfig.DLQ_QUEUE)
    public void processFailedMessage(GradeMessage failedMessage) {
        log.error("MESSAGE SENT TO DLQ (Processing Failed): {}", failedMessage);
        // Here you could save to a 'failed_jobs' table or send an alert email
    }
}