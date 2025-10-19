package com.example.lab03.Compulsory;

import org.springframework.stereotype.Component;

@Component
public class FourthDependency {
    public FourthDependency(){
        System.out.println("Constructor of FourthDependency");
    }

    public String getValue() {
        return "This is the FourthDependency! Method Injection";
    }
}
