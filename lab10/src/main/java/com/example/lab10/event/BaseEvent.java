package com.example.lab10.event;

import java.math.BigDecimal;

public abstract class BaseEvent {
    public final String aggregateId;

    public BaseEvent(String aggregateId) {
        this.aggregateId = aggregateId;
    }
}
