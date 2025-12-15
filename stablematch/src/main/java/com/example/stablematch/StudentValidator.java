package com.example.stablematch;

import com.example.stablematch.dto.VerificationRequestEvent;
import com.example.stablematch.dto.VerificationResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentValidator {
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "verification_queue")
    public void validateStudent(VerificationRequestEvent event) {
        System.out.println("Validating student: " + event.getName());

        boolean approved = event.getGpa() != null && event.getGpa() >= 5.0;
        String reason = approved ? "Good academic standing" : "GPA too low";

        VerificationResultEvent result = new VerificationResultEvent(
                event.getStudentId(), approved, reason
        );
        System.out.println("Status: " + reason);

        rabbitTemplate.convertAndSend("saga_exchange", "verification.result", result);
    }
}