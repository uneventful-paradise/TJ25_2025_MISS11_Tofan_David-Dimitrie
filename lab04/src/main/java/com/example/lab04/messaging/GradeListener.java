package com.example.lab04.messaging;

import com.example.lab04.Course;
import com.example.lab04.CourseType;
import com.example.lab04.Grade;
import com.example.lab04.Student;
import com.example.lab04.config.RabbitMQConfig;
import com.example.lab04.dto.GradeMessage;
import com.example.lab04.repository.CourseRepository;
import com.example.lab04.repository.GradeRepository;
import com.example.lab04.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GradeListener {

    private static final Logger log = LoggerFactory.getLogger(GradeListener.class);
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final GradeRepository gradeRepository;

    public GradeListener(StudentRepository studentRepository, CourseRepository courseRepository, GradeRepository gradeRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.gradeRepository = gradeRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    @Transactional
    public void consumeMessage(GradeMessage message) {
        log.info("Processing grade for student: {}", message.getStudentCode());

        // 1. DLQ Testing Logic: Throw exception for invalid grades
        if (message.getGrade() < 1 || message.getGrade() > 10) {
            throw new IllegalArgumentException("Invalid grade value: " + message.getGrade());
        }

        // 2. Find Entities
        Student student = studentRepository.findByCode(message.getStudentCode())
                .orElseThrow(() -> new RuntimeException("Student not found: " + message.getStudentCode()));

        Course course = courseRepository.findByCode(message.getCourseCode()) // You need to add findByCode to CourseRepo
                .orElseThrow(() -> new RuntimeException("Course not found: " + message.getCourseCode()));

        // 3. Business Rule: Only Compulsory Courses
        if (course.getType() == CourseType.compulsory) {
            Grade grade = new Grade(student, course, message.getGrade());
            gradeRepository.save(grade);
            log.info("Saved compulsory grade for {}", message.getStudentCode());
        } else {
            log.warn("Ignored optional course grade: {}", message.getCourseCode());
        }
    }
}