package com.example.lab04.dto;

import com.example.lab04.CourseType;
import lombok.Data;

@Data
public class CourseResponseDto {
    private Long id;
    private CourseType type;
    private String code;
    private String abbr;
    private String name;
    private String description;
    private String instructorName;
    private String packName;
}