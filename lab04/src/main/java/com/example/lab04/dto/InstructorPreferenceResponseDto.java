package com.example.lab04.dto;

import lombok.Data;

@Data
public class InstructorPreferenceResponseDto {
    private Long id;
    private Long optionalCourseId;
    private String optionalCourseName;
    private Long compulsoryCourseId;
    private String compulsoryCourseName;
    private Double percentage;
}