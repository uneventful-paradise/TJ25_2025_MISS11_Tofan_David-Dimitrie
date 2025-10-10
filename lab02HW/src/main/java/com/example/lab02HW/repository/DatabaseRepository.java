package com.example.lab02HW.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getDatabaseName() {
        try{
            return jdbcTemplate.queryForObject("select current_database()", String.class);
        } catch(Exception e){
            return "Error fetvhing name: " + e.getMessage();
        }
    }
}
