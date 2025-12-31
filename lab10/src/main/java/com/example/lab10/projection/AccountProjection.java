package com.example.lab10.projection;

import com.example.lab10.event.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountProjection {

    private final JdbcTemplate jdbcTemplate;

    public AccountProjection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void project(BaseEvent event) {
        if (event instanceof AccountCreatedEvent e) {
            jdbcTemplate.update("INSERT INTO account_view (account_id, owner, balance) VALUES (?, ?, 0.00)",
                    e.aggregateId, e.owner);
        } else if (event instanceof MoneyDepositedEvent e) {
            jdbcTemplate.update("UPDATE account_view SET balance = balance + ? WHERE account_id = ?",
                    e.amount, e.aggregateId);
        }
    }
}