package com.example.lab04.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InstructorPreferenceRequestDto {

    @NotNull(message = "Optional Course ID is required")
    private Long optionalCourseId;

    @NotNull(message = "Compulsory Course ID is required")
    private Long compulsoryCourseId;

    @NotNull(message = "Percentage is required")
    @Min(value = 0, message = "Percentage cannot be negative")
    @Max(value = 100, message = "Percentage cannot exceed 100")
    private Double percentage;
}