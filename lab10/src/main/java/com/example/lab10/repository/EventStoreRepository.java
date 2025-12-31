package com.example.lab10.repository;

import com.example.lab10.aggregate.AccountAggregate;
import com.example.lab10.event.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EventStoreRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public EventStoreRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public void save(AccountAggregate aggregate) {
        int currentVersion = aggregate.getVersion();

        for (BaseEvent event : aggregate.getUncommittedChanges()) {
            currentVersion++;
            try {
                String eventData = objectMapper.writeValueAsString(event);
                String eventType = event.getClass().getSimpleName();

                jdbcTemplate.update(
                        "INSERT INTO event_store (aggregate_id, event_type, event_data, version) VALUES (?, ?, ?, ?)",
                        aggregate.getId(), eventType, eventData, currentVersion
                );
            } catch (Exception e) {
                throw new RuntimeException("Error serializing event", e);
            }
        }

        // save snapshots every 5 seconds
        if (currentVersion % 5 == 0) {
            saveSnapshot(aggregate, currentVersion);
        }
    }

    private void saveSnapshot(AccountAggregate aggregate, int version) {
        try {
            String stateData = objectMapper.writeValueAsString(aggregate);
            jdbcTemplate.update(
                    "INSERT INTO snapshots (aggregate_id, state_data, version) VALUES (?, ?, ?)",
                    aggregate.getId(), stateData, version
            );
        } catch (Exception e) {
            throw new RuntimeException("Error saving snapshot", e);
        }
    }

    public AccountAggregate load(String aggregateId) {
        AccountAggregate aggregate;
        int currentVersion = 0;

        // load snapshot with highest version
        List<AccountAggregate> snapshots = jdbcTemplate.query(
                "SELECT state_data, version FROM snapshots WHERE aggregate_id = ? ORDER BY version DESC LIMIT 1",
                (rs, rowNum) -> {
                    try {
                        return objectMapper.readValue(rs.getString("state_data"), AccountAggregate.class);
                    } catch (Exception e) { return null; }
                },
                aggregateId
        );

        if (!snapshots.isEmpty() && snapshots.get(0) != null) {
            aggregate = snapshots.get(0);
            currentVersion = aggregate.getVersion();
        } else {
            aggregate = new AccountAggregate(); // Start fresh
        }

        // load event after snapshot version
        List<BaseEvent> history = jdbcTemplate.query(
                "SELECT event_type, event_data FROM event_store WHERE aggregate_id = ? AND version > ? ORDER BY version ASC",
                (rs, rowNum) -> {
                    try {
                        String type = rs.getString("event_type");
                        String data = rs.getString("event_data");
                        Class<?> eventClass = Class.forName("com.example.lab10.event." + type);
                        return (BaseEvent) objectMapper.readValue(data, eventClass);
                    } catch (Exception e) { throw new RuntimeException(e); }
                },
                aggregateId, currentVersion
        );

        // get to current state
        aggregate.replay(history);

        return aggregate;
    }
}