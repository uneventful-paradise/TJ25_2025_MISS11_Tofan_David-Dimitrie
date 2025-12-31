package com.example.lab10.aggregate;

import com.example.lab10.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountAggregate {
    private String id;
    private String owner;
    private BigDecimal balance = BigDecimal.ZERO;
    private int version = 0; // number of events applied
    private final List<BaseEvent> changes = new ArrayList<>(); // uncommitted events

    public AccountAggregate() {}

    public AccountAggregate(String id, String owner) {
        apply(new AccountCreatedEvent(id, owner));
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        apply(new MoneyDepositedEvent(this.id, amount));
    }

    // create event . apply to state . add to changes list
    private void apply(BaseEvent event) {
        this.changes.add(event);
        applyStateChange(event);
    }

    // replay history
    public void replay(List<BaseEvent> history) {
        for (BaseEvent event : history) {
            applyStateChange(event);
            this.version++;
        }
    }

    private void applyStateChange(BaseEvent event) {
        if (event instanceof AccountCreatedEvent e) {
            this.id = e.aggregateId;
            this.owner = e.owner;
            this.balance = BigDecimal.ZERO;
        } else if (event instanceof MoneyDepositedEvent e) {
            this.balance = this.balance.add(e.amount);
        }
    }

    public String getId() { return id; }
    public BigDecimal getBalance() { return balance; }
    public int getVersion() { return version; }
    public List<BaseEvent> getUncommittedChanges() { return changes; }
    public void markChangesAsCommitted() {
        this.version += changes.size();
        changes.clear();
    }
}