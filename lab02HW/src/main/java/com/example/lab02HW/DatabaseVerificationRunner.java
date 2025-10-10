package com.example.lab02HW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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

        System.out.println("\n\n\n=======================================================");
        System.out.println("✅ Application Started Successfully!");
        System.out.println("-------------------------------------------------------");
        System.out.println("   Active Profile: " + activeProfile.toUpperCase());
        System.out.println("   Greeting Message: '" + greetingMessage + "' (Demonstrates property precedence)");
        System.out.println("   DB Value for 'environment.name': " + envName);
        System.out.println("=======================================================\n\n\n");
    }
}
