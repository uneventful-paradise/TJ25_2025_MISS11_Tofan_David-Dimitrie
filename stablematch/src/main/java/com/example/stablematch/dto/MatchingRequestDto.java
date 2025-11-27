package com.example.stablematch.dto;

import lombok.Data;
import java.util.List;

@Data
public class MatchingRequestDto {
    private List<StudentDto> students;
    private List<CourseDto> courses;
}