package com.example.lab10.event;

public class AccountCreatedEvent extends BaseEvent {
    public final String owner;
    public AccountCreatedEvent(String aggregateId, String owner) {
        super(aggregateId);
        this.owner = owner;
    }
}