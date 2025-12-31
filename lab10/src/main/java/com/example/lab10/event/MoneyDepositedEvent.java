package com.example.lab10.event;

import java.math.BigDecimal;

public class MoneyDepositedEvent extends BaseEvent {
    public final BigDecimal amount;
    public MoneyDepositedEvent(String aggregateId, BigDecimal amount) {
        super(aggregateId);
        this.amount = amount;
    }
}