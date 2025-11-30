package com.example.lab04.controller;

import com.example.lab04.Assignment;
import com.example.lab04.dto.solver.SolverResponseDto;
import com.example.lab04.repository.AssignmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentRepository assignmentRepository;

    @GetMapping
    @Operation(summary = "Get all assignments")
    public List<SolverResponseDto> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get assignment for a specific student")
    public SolverResponseDto getAssignmentByStudent(@PathVariable Long studentId) {
        Assignment assignment = assignmentRepository.findByStudent_Id(studentId)
                .orElseThrow(() -> new RuntimeException("No assignment found for student " + studentId));
        return mapToDto(assignment);
    }

    private SolverResponseDto mapToDto(Assignment assignment) {
        SolverResponseDto dto = new SolverResponseDto();
        dto.setStudentId(assignment.getStudent().getId());
        dto.setStudentName(assignment.getStudent().getUser().getName());
        dto.setCourseId(assignment.getCourse().getId());
        dto.setCourseName(assignment.getCourse().getName());
        return dto;
    }
}