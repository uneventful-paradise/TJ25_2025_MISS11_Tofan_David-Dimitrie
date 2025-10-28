package com.example.lab04;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Locale;

@SpringBootApplication
public class Lab04Application {
    private static final Logger log = LoggerFactory.getLogger(Lab04Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Lab04Application.class, args);
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

        Faker faker = new Faker(new Locale("en-US"));

        Instructor prof1 = instructorService.save(new Instructor(
                faker.name().fullName(),
                faker.internet().emailAddress()
        ));
        Instructor prof2 = instructorService.save(new Instructor(
                faker.name().fullName(),
                faker.internet().emailAddress()
        ));
        log.info("saved instructors: {} and {}", prof1.getName(), prof2.getName());

        Pack pack1 = packService.save(new Pack(3, 1, "Licenta An 3, Sem 1"));
        log.info("saved pack: {}", pack1.getName());

        String courseCode = "CS101";

        log.info("TESTING CREATE OPERATION FOR COURSES");
        Course newCourse = new Course(
                CourseType.compulsory,
                courseCode,
                "IP",
                "Ingineria Programarii",
                4,
                faker.lorem().sentence(),
                prof1,
                pack1
        );
        courseService.save(newCourse);
        log.info("Created new course: {}", newCourse.getName());

        log.info("TESTING READ OPERATION FOR COURSES");
        courseService.findById(newCourse.getId()).ifPresent(course ->
                log.info("findById(1L): Found course: {}", course.toString())
        );

        // READ (Test Derived Query) ERROR HERE
        List<Course> compulsoryCourses = courseService.findByType(CourseType.compulsory);
        log.info("findByType('compulsory'): Found {} courses.", compulsoryCourses.size());

        log.info("TESTING UPDATE OPERATION FOR COURSES");
        String newDescription = "Cel mai smecher curs.";
        int updatedRows = courseService.updateDescription(courseCode, newDescription);
        log.info("updateDescription: updated {} rows.", updatedRows);

        courseService.findById(newCourse.getId()).ifPresent(course ->
                log.info("Verified description: {}", course.getDescription())
        );

        log.info("TESTING DELETE OPERATION FOR COURSES");
        courseService.deleteById(newCourse.getId());
        log.info("Deleted course. Remaining courses: {}", courseService.count());

        log.info("TESTING JPQL QUERY");
        log.info("Instructors with 'a' in their name:");
        List<Instructor> instructors = instructorService.findByName("a");
        instructors.forEach(instructor -> log.info("Found: {}", instructor.getName()));

        log.info("\nDEMO FINISHED\n");
    }
}