package com.example.lab04.dto;

import lombok.Data;

@Data
public class PreferenceResponseDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
    private int rank;
}