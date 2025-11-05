package com.example.lab04.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    @PostMapping("/login")
    public Map<String, String> mockLogin() {
        return Map.of("message", "Login successful");
    }
}