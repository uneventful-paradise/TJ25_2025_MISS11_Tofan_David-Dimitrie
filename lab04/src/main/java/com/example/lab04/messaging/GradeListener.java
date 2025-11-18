package com.example.lab04.messaging;

import com.example.lab04.dto.GradeMessage;
import com.example.lab04.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GradeListener {

    private static final Logger log = LoggerFactory.getLogger(GradeListener.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumeMessage(GradeMessage message) {
        log.info("NEW MESSAGE RECEIVED FROM QUEUE:");
        log.info("Student: " + message.getStudentCode());
        log.info("Course: " + message.getCourseCode());
        log.info("Grade: " + message.getGrade());
    }
}