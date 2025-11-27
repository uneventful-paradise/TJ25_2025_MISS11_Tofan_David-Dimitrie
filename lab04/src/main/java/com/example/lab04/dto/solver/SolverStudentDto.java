package com.example.lab04.dto.solver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolverStudentDto {
    private Long id;
    private String name;
    private List<Long> preferences;
}