package com.example.lab04.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; // <-- Import this
import lombok.Data;

@Data
public class StudentRequestDto {

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // --- ADD THIS FIELD ---
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    // ---------------------

    @NotNull(message = "Year is required")
    private Integer year;

    @NotNull(message = "Pack ID is required")
    private Long packId;
}