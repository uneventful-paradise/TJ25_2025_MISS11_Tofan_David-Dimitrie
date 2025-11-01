package com.example.lab04.dto;

import lombok.Data;

@Data
public class PackResponseDto {
    private Long id;
    private Integer year;
    private Integer semester;
    private String name;
}