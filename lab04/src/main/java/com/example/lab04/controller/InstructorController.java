package com.example.lab04.controller;

import com.example.lab04.Instructor;
import com.example.lab04.dto.InstructorResponseDto;
import com.example.lab04.service.InstructorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstructorResponseDto createInstructor(@RequestBody Instructor instructor) {
        return instructorService.save(instructor);
    }

    @GetMapping
    public List<InstructorResponseDto> getAllInstructors() {
        return instructorService.findAll();
    }
}