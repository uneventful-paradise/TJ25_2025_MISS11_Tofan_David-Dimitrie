package com.example.lab04.dto;

import com.example.lab04.CourseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseRequestDto {

    @NotNull(message = "Course type cannot be null")
    private CourseType type;

    @NotBlank(message = "Course code is required")
    @Size(max = 50)
    private String code;

    @Size(max = 20)
    private String abbr;

    @NotBlank(message = "Course name is required")
    private String name;

    private Integer groupCount = 1;

    private String description;

    @NotNull(message = "Instructor ID cannot be null")
    private Long instructorId;

    @NotNull(message = "Pack ID cannot be null")
    private Long packId;
}