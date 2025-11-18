package com.example.lab02HW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DatabaseVerificationRunner implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Value("${app.greeting}")
    private String greetingMessage;

    @Override
    public void run(String... args) throws Exception {
        String envName = jdbcTemplate.queryForObject(
                "SELECT config_value FROM app_config WHERE config_key = 'environment.name'", String.class
        );

        System.out.println("\n\n\n");
        System.out.println("Am pornit sefu!");
        System.out.println(" ");
        System.out.println("Active Profile: " + activeProfile);
        System.out.println("Greeting Message: '" + greetingMessage);
//        System.out.println("DB Value for 'environment.name': " + envName);
        System.out.println("\n\n\n");

//        try {
//
//            List<Map<String, Object>> tableContents = jdbcTemplate.queryForList(
//                    "SELECT * FROM app_config"
//            );
//
//            System.out.println("   Contents of 'app_config' table:");
//            if (tableContents.isEmpty()) {
//                System.out.println("   -> Table is empty.");
//            } else {
//                // Loop through each row (which is a Map) and print it
//                for (Map<String, Object> row : tableContents) {
//                    System.out.println("   -> " + row);
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println("   Error querying database: " + e.getMessage());
//        }
    }
}
