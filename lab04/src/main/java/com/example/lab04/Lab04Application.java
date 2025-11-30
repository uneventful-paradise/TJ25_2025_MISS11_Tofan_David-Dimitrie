package com.example.lab04;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.example.lab04.service.CourseService;
import com.example.lab04.service.InstructorService;
import com.example.lab04.service.PackService;
import com.example.lab04.service.StudentService;
import com.github.javafaker.Faker;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "E-Learning API", version = "1.0",
        description = "API for managing Students, Courses, and Preferences"))
public class Lab04Application {
    private static final Logger log = LoggerFactory.getLogger(Lab04Application.class);

    public static void main(String[] args) {
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        SpringApplication.run(Lab04Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner runFakerDemo(
            CourseService courseService,
            InstructorService instructorService,
            PackService packService,
            StudentService studentService
    ) {
        return (args) -> {
            runDemo(courseService, instructorService, packService, studentService);

        };

    }

    @Transactional
    public void runDemo(CourseService courseService, InstructorService instructorService, PackService packService, StudentService studentService) {
        log.info("\nSTARTING DEMO\n");
        courseService.deleteAll();
        packService.deleteAll();
        instructorService.deleteAll();
        studentService.deleteAll();
        log.info("cleared repo info");

        log.info("\nDEMO FINISHED\n");
    }
}