package com.example.lab04;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findByCode(String code) {
        return studentRepository.findByCode(code);
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + id));
    }

    public Student update(Long id, Student studentDetails) {
        // First, find the existing student
        Student existingStudent = findById(id);

        // Update the fields
        existingStudent.setName(studentDetails.getName());
        existingStudent.setEmail(studentDetails.getEmail());
        existingStudent.setCode(studentDetails.getCode());
        existingStudent.setYear(studentDetails.getYear());

        // Save the updated entity
        return studentRepository.save(existingStudent);
    }

    public void deleteById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    public void deleteAll() {
        studentRepository.deleteAll();
    }
}