package com.example.lab03.DomainModel;

public class Customer {
    private Long id;
    private String name;
    private boolean isLoyal;

    public Customer(long id, String name, boolean isLoyal) {
        this.id = id;
        this.name = name;
        this.isLoyal = isLoyal;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public boolean isLoyal() {
        return isLoyal;
    }
}
