package com.example.lab04.dto.solver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolverRequestDto {
    private List<SolverStudentDto> students;
    private List<SolverCourseDto> courses;
}
