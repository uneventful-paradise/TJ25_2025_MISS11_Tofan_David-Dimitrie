package com.example.stablematch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchingResponseDto {
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
}