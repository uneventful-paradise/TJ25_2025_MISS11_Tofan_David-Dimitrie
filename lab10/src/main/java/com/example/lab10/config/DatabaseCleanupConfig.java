package com.example.lab10.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class DatabaseCleanupConfig {

    @Bean
    public CommandLineRunner cleanMongo(MongoTemplate mongoTemplate) {
        return args -> {
            mongoTemplate.dropCollection("account_views");
            System.out.println("MongoDB 'account_views' cleared");
        };
    }
}