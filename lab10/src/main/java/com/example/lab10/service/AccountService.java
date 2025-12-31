package com.example.lab10.service;

import com.example.lab10.aggregate.AccountAggregate;
import com.example.lab10.event.BaseEvent;
import com.example.lab10.projection.AccountProjection;
import com.example.lab10.repository.EventStoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {

    private final EventStoreRepository repository;
    private final AccountProjection projection;

    public AccountService(EventStoreRepository repository, AccountProjection projection) {
        this.repository = repository;
        this.projection = projection;
    }

    @Transactional
    public String createAccount(String owner) {
        String id = UUID.randomUUID().toString();
        AccountAggregate account = new AccountAggregate(id, owner);
        saveAndProject(account);
        return id;
    }

    @Transactional
    public void deposit(String id, BigDecimal amount) {
        AccountAggregate account = repository.load(id);
        account.deposit(amount);
        saveAndProject(account);
    }

    // helper for write and read
    private void saveAndProject(AccountAggregate account) {
        // update read (projection)
        for (BaseEvent event : account.getUncommittedChanges()) {
            projection.project(event);
        }

        // save write (snapshot)
        repository.save(account);

        account.markChangesAsCommitted();
    }
}