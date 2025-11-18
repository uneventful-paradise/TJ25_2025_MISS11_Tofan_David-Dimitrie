package com.example.lab04.dto;

import lombok.Data;

@Data
public class GradeResponseDto {
    private Long id;
    private String studentName;
    private String courseName;
    private Double value;
}