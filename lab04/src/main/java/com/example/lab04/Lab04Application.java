package com.example.lab04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Lab04Application {
    private static final Logger log = LoggerFactory.getLogger(Lab04Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Lab04Application.class, args);
    }

    @Bean
    public CommandLineRunner simpleTest(StudentRepository studentRepository) {
        return (args) -> {
            log.info("\nSTARTING SIMPLE TEST\n");
            studentRepository.deleteAll();
            log.info("cleared repo");

            Student david = new Student("S1001", "Tofan David", "davidtofan7@gmail.com", 3);
            Student andrei = new Student("S1002", "Rusu Andrei", "bubblered03@gmail.com", 3);

            studentRepository.save(david);
            studentRepository.save(andrei);
            log.info("inserted students");

            log.info("student info:\n");
            List<Student> allStudents = studentRepository.findAll();
            allStudents.forEach(student -> log.info(student.toString()));

            log.info("student S1001");
            studentRepository.findByCode("S1001").ifPresent(student -> {
                log.info("Found student: " + student.toString());
            });

            log.info("\nSIMPLE TEST FINISHED\n");
        };
    }

}
