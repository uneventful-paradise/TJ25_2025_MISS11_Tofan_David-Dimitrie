package com.example.lab04.dto.solver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolverCourseDto {
    private Long id;
    private String name;
    private int capacity;
    private List<Long> preferredStudentIds;
}