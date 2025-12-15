package com.example.lab04.dto;

import lombok.Data;

@Data
public class StudentResponseDto {
    private Long id;
    private String code;
    private String name;
    private String email;
    private Integer year;
    private Long packId;
    private String packName;
    private String status;
}