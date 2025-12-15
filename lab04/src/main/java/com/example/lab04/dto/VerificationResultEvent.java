package com.example.lab04.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class VerificationResultEvent {
    private Long studentId;
    private boolean isApproved;
    private String reason;
}