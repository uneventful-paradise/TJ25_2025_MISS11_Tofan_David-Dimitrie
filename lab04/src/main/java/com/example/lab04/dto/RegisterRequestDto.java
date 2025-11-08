package com.example.lab04.dto;
import com.example.lab04.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6)
    private String password;
    @NotBlank
    private String name;
    @NotNull
    private Role role;
}