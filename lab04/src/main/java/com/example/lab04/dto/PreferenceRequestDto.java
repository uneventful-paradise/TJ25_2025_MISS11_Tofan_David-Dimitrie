package com.example.lab04.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PreferenceRequestDto {

    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @NotNull(message = "Course ID cannot be null")
    private Long courseId;

    @NotNull(message = "Rank cannot be null")
    @Min(value = 1, message = "Rank must be at least 1")
    private Integer rank;
}