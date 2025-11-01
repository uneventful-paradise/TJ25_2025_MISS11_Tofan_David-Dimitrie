package com.example.lab04.controller;

import com.example.lab04.dto.CourseRequestDto;
import com.example.lab04.dto.CourseResponseDto;
import com.example.lab04.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponseDto createCourse(@Valid @RequestBody CourseRequestDto courseDto) {
        return courseService.createCourse(courseDto);
    }

    @GetMapping
    public List<CourseResponseDto> getAllCourses() {
        return courseService.findAll();
    }
}