package com.example.lab10.controller;

import com.example.lab10.service.AccountService;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;
    private final JdbcTemplate jdbcTemplate;

    public AccountController(AccountService service, JdbcTemplate jdbcTemplate) {
        this.service = service;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping
    public String create(@RequestParam String owner) {
        return service.createAccount(owner);
    }

    @PostMapping("/{id}/deposit")
    public void deposit(@PathVariable String id, @RequestParam BigDecimal amount) {
        service.deposit(id, amount);
    }

    // query for projection
    @GetMapping("/{id}")
    public Map<String, Object> getAccount(@PathVariable String id) {
        return jdbcTemplate.queryForMap("SELECT * FROM account_view WHERE account_id = ?", id);
    }
}