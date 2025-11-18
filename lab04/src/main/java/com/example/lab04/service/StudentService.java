package com.example.lab04.service;

import com.example.lab04.Pack;
import com.example.lab04.Role;
import com.example.lab04.Student;
import com.example.lab04.User;
import com.example.lab04.dto.StudentRequestDto;
import com.example.lab04.dto.StudentResponseDto;
import com.example.lab04.exception.ResourceNotFoundException;
import com.example.lab04.repository.PackRepository;
import com.example.lab04.repository.StudentRepository;
import com.example.lab04.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final PackRepository packRepository;
    // FIX: Need UserRepository and PasswordEncoder to create the User
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, PackRepository packRepository,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.packRepository = packRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    public StudentService(StudentRepository studentRepository) {
//        this.studentRepository = studentRepository;
//    }
    @Transactional(readOnly = true) // Good for read-only operations
    public List<StudentResponseDto> findAllDto() {
        return studentRepository.findAll().stream()
                .map(this::mapToResponseDto) // Uses your existing helper
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentResponseDto findByIdDto(Long id) {
        Student student = findById(id); // Uses your existing findById
        return mapToResponseDto(student);
    }
    @Transactional
    public StudentResponseDto save(StudentRequestDto dto) {
        Pack pack = packRepository.findById(dto.getPackId())
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found with id: " + dto.getPackId()));

        // --- This is the logic you want ---
        // 1. Create and save the new User
        User newUser = new User(
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()), // <-- Use the password from the DTO
                dto.getName(),
                Role.ROLE_STUDENT // This service only registers Students
        );
        User savedUser = userRepository.save(newUser);

        // 2. Create and save the new Student profile
        Student student = new Student(
                savedUser,
                dto.getCode(),
                dto.getYear(),
                pack
        );
        Student savedStudent = studentRepository.save(student);

        // 3. Return the clean DTO
        return mapToResponseDto(savedStudent);
    }

    private StudentResponseDto mapToResponseDto(Student student) {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId());
        dto.setCode(student.getCode());
        // FIX: 'name' and 'email' are now on the User object
        dto.setName(student.getUser().getName());
        dto.setEmail(student.getUser().getEmail());
        dto.setYear(student.getYear());
        dto.setPackId(student.getPack().getId());
        dto.setPackName(student.getPack().getName());
        return dto;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findByCode(String code) {
        return studentRepository.findByCode(code);
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    @Transactional
    public StudentResponseDto update(Long id, StudentRequestDto dto) {
        // 1. Find the existing student
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // 2. Find the new Pack (if it changed)
        Pack pack = packRepository.findById(dto.getPackId())
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found with id: " + dto.getPackId()));

        // 3. Update the fields on the existing, managed entity
        existingStudent.setCode(dto.getCode());
        existingStudent.getUser().setName(dto.getName());
        existingStudent.getUser().setEmail(dto.getEmail());
        existingStudent.setYear(dto.getYear());
        existingStudent.setPack(pack);

        // 4. Save the changes
        Student updatedStudent = studentRepository.save(existingStudent);

        // 5. Map to the DTO and return
        return mapToResponseDto(updatedStudent);
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