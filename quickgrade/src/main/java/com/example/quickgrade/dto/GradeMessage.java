package com.example.quickgrade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GradeMessage {
    private String studentCode;
    private String courseCode;
    private Double grade;
}