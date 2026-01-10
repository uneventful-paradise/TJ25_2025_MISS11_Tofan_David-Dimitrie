package com.example.lab04.config;

import com.example.lab04.service.CourseService;
import com.example.lab04.service.InstructorService;
import com.example.lab04.service.PackService;
import com.example.lab04.service.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Profile("!test")
public class DataInitializer {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
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
