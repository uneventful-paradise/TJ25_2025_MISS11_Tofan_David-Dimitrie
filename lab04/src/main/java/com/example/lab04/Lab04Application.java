package com.example.lab04;
import com.github.javafaker.Faker;
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
            log.info("\nSTARTING FAKER DEMO & CRUD TEST\n");

            // Must delete in the correct order due to foreign keys
            courseService.deleteAll();
            packService.deleteAll();
            instructorService.deleteAll();
            studentService.deleteAll();
            log.info("Cleared all repositories.");

            Faker faker = new Faker(new Locale("en-US"));

            Instructor prof1 = instructorService.save(new Instructor(
                    faker.name().fullName(),
                    faker.internet().emailAddress()
            ));
            Instructor prof2 = instructorService.save(new Instructor(
                    faker.name().fullName(),
                    faker.internet().emailAddress()
            ));
            log.info("Saved instructors: {} and {}", prof1.getName(), prof2.getName());

            Pack pack1 = packService.save(new Pack(3, 1, "Licenta An 3, Sem 1"));
            log.info("Saved pack: {}", pack1.getName());

            String courseCode = "CS101";

            log.info("--- CREATE ---");
            Course newCourse = new Course(
                    CourseType.compulsory,
                    courseCode,
                    "IntroCS",
                    "Introduction to Computer Science",
                    4,
                    faker.lorem().sentence(),
                    prof1,
                    pack1
            );
            courseService.save(newCourse);
            log.info("Created new course: {}", newCourse.getName());

            log.info("--- READ ---");
            courseService.findById(newCourse.getId()).ifPresent(course ->
                    log.info("findById(1L): Found course: {}", course.toString())
            );

            // READ (Test Derived Query) ERROR HERE
            List<Course> compulsoryCourses = courseService.findByType(CourseType.compulsory);
            log.info("findByType('compulsory'): Found {} courses.", compulsoryCourses.size());

            // UPDATE
            log.info("--- UPDATE ---");
            String newDescription = "A new, updated description.";
            int updatedRows = courseService.updateDescription(courseCode, newDescription);
            log.info("updateDescription(): Updated {} rows.", updatedRows);

            // Verify the update
            courseService.findById(newCourse.getId()).ifPresent(course ->
                    log.info("Verified description: {}", course.getDescription())
            );

            // DELETE
            log.info("--- DELETE ---");
            courseService.deleteById(newCourse.getId());
            log.info("Deleted course. Remaining courses: {}", courseService.count());

            // --- 3. Test JPQL Query ---
            log.info("--- JPQL TEST ---");
            log.info("Finding instructors with 'a' in their name:");
            List<Instructor> instructors = instructorService.findByName("a");
            instructors.forEach(instructor -> log.info("Found: {}", instructor.getName()));

            log.info("\nFAKER DEMO FINISHED\n");
        };
    }
}