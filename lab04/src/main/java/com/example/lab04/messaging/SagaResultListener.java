package com.example.lab04.messaging;

import com.example.lab04.dto.VerificationResultEvent;
import com.example.lab04.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SagaResultListener {
    private final StudentRepository repository;

    @RabbitListener(queues = "result_queue")
    public void handleResult(VerificationResultEvent event) {
        System.out.println("Received Result for Student " + event.getStudentId());

        repository.findById(event.getStudentId()).ifPresent(student -> {
            student.setStatus(event.isApproved() ? "APPROVED" : "REJECTED");
            repository.save(student);
        });
    }
}