package com.example.lab03.Compulsory;

import org.springframework.stereotype.Component;

@Component
public class SecondDependency {
    public SecondDependency(){
        System.out.println("Constructor of SecondDependency");
    }

    public String getValue() {
        return "This is the Second Dependency! Field Injection";
    }
}
