package com.example.stablematch.dto;

import lombok.Data;
import java.util.List;

@Data
public class StudentDto {
    private Long id;
    private String name;
    private List<Long> preferences;
}