package com.example.lab03.Compulsory;

import org.springframework.stereotype.Component;

@Component
public class ThirdDependency {
    public ThirdDependency() {
        System.out.println("Constructor of ThirdDependency");
    }

    public String getValue() {
        return "This is the Second Dependency! Setter Injection";
    }
}
