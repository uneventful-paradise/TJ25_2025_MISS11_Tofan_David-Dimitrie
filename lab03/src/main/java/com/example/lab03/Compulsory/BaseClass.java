package com.example.lab03.Compulsory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dependency_test")
@Component
public class BaseClass {
    private final FirstDependency firstDependency;

    //field injection
    @Autowired
    private SecondDependency secondDependency;

    private ThirdDependency thirdDependency;
    private FourthDependency fourthDependency;

    //setter injection
    @Autowired
    public void setThirdDependency(ThirdDependency thirdDependency) {
        System.out.println("Setting ThirdDependency");
        this.thirdDependency = thirdDependency;
    }

    //constructor injection
    public BaseClass(FirstDependency firstDependency) {
        System.out.println("Constructor of BaseClass");
        this.firstDependency = firstDependency;
//        this.thirdDependency = thirdDependency;
    }

    //method injection
    @Autowired
    public void fourthDependencyCaller(FourthDependency fourthDependency) {
        this.fourthDependency = fourthDependency;
        System.out.println("FourthDependency Called");
    }

    public void printDependencies() {
        System.out.println(firstDependency.getValue());
        System.out.println(secondDependency.getValue());
        System.out.println(thirdDependency.getValue());
        System.out.println(fourthDependency.getValue());
    }
}
