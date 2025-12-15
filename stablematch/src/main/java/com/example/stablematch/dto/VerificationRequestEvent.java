package com.example.stablematch.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class VerificationRequestEvent {
    private Long studentId;
    private String name;
    private Double gpa;
}