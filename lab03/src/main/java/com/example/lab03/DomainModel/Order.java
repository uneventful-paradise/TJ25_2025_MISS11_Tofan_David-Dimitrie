package com.example.lab03.DomainModel;

import java.math.BigDecimal;

public class Order {
    private Long id;
    private Customer customer;
    private BigDecimal value;

    public Order(Long id, Customer customer, BigDecimal amount) {
        this.id = id;
        this.customer = customer;
        this.value = amount;
    }

    public Long getId() {
        return id;
    }
    public Customer getCustomer() {
        return customer;
    }
    public BigDecimal getValue() {
        return value;
    }
}
