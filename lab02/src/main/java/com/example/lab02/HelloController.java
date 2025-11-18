package com.example.lab02;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
class HelloController {
    @Value("${app.greeting}")
    private String greeting;

    @GetMapping("/hello")
    public String hello() {
        return greeting;
    }
}
