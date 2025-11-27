package com.example.stablematch.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseDto {
    private Long id;
    private String name;
    private int capacity;
    private List<Long> preferredStudentIds;
}