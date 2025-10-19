package com.example.lab03.Compulsory;

import org.springframework.stereotype.Component;

@Component
public class FirstDependency {
    public FirstDependency() {
        System.out.println("Constructor of firstDependency");
    }

    public String getValue() {
        return "This is the First Dependency! Construction Injection";
    }
}
