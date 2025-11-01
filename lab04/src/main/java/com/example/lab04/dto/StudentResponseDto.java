package com.example.lab04.dto;

import lombok.Data;

@Data
public class StudentResponseDto {
    private Long id;
    private String code;
    private String name;
    private String email;
    private Integer year;
    private Long packId; // Just send the ID, not the whole object
    private String packName; // Send a human-readable name
}