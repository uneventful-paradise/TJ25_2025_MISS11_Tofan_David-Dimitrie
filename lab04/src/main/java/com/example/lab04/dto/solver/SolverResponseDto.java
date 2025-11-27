package com.example.lab04.dto.solver;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolverResponseDto {
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
}