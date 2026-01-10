package com.example.lab04.repository;

import com.example.lab04.Pack;
import com.example.lab04.Role;
import com.example.lab04.Student;
import com.example.lab04.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackRepository packRepository;

    @Test
    void testSchemaCreationAndCrud() {
        Pack pack = new Pack(3, 1, "Computer Science");
        packRepository.save(pack);

        User user = new User("test@test.com", "password", "Test User", Role.ROLE_STUDENT);
        userRepository.save(user);

        Student student = new Student(user, "S100", 3, pack);
        student.setGpa(9.5);

        Student savedStudent = studentRepository.save(student);

        assertThat(savedStudent.getId()).isNotNull();

        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());

        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getCode()).isEqualTo("S100");

        savedStudent.setGpa(10.0);
        Student updatedStudent = studentRepository.save(savedStudent);

        assertThat(updatedStudent.getGpa()).isEqualTo(10.0);

        studentRepository.delete(updatedStudent);
        
        Optional<Student> deletedStudent = studentRepository.findById(savedStudent.getId());
        assertThat(deletedStudent).isEmpty();
    }
}