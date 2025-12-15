package com.example.lab04.service;

import com.example.lab04.Pack;
import com.example.lab04.Role;
import com.example.lab04.Student;
import com.example.lab04.User;
import com.example.lab04.dto.StudentRequestDto;
import com.example.lab04.dto.StudentResponseDto;
import com.example.lab04.dto.VerificationRequestEvent;
import com.example.lab04.exception.ResourceNotFoundException;
import com.example.lab04.repository.PackRepository;
import com.example.lab04.repository.StudentRepository;
import com.example.lab04.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    public StudentService(StudentRepository studentRepository, PackRepository packRepository,
                          UserRepository userRepository, PasswordEncoder passwordEncoder, RabbitTemplate rabbitTemplate) {
        this.studentRepository = studentRepository;
        this.packRepository = packRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rabbitTemplate = rabbitTemplate;
    }

//    public StudentService(StudentRepository studentRepository) {
//        this.studentRepository = studentRepository;
//    }
    @Transactional(readOnly = true)
    public List<StudentResponseDto> findAllDto() {
        return studentRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentResponseDto findByIdDto(Long id) {
        Student student = findById(id);
        return mapToResponseDto(student);
    }
    @Transactional
    public StudentResponseDto save(StudentRequestDto dto) {
        Pack pack = packRepository.findById(dto.getPackId())
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found with id: " + dto.getPackId()));

        User newUser = new User(
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getName(),
                Role.ROLE_STUDENT
        );
        User savedUser = userRepository.save(newUser);

        Student student = new Student(
                savedUser,
                dto.getCode(),
                dto.getYear(),
                pack
        );
        student.setGpa(dto.getGpa());
        student.setStatus("PENDING");
        Student savedStudent = studentRepository.save(student);

        VerificationRequestEvent event = new VerificationRequestEvent(
                savedStudent.getId(),
                savedStudent.getUser().getName(),
                savedStudent.getGpa() // or GPA, depending on what you need
        );

        // 4. Send to RabbitMQ
        rabbitTemplate.convertAndSend("saga_exchange", "verification.request", event);

        return mapToResponseDto(savedStudent);
    }

    private StudentResponseDto mapToResponseDto(Student student) {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId());
        dto.setCode(student.getCode());
        dto.setName(student.getUser().getName());
        dto.setEmail(student.getUser().getEmail());
        dto.setYear(student.getYear());
        dto.setPackId(student.getPack().getId());
        dto.setPackName(student.getPack().getName());
        dto.setStatus(student.getStatus());
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
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        Pack pack = packRepository.findById(dto.getPackId())
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found with id: " + dto.getPackId()));

        existingStudent.setCode(dto.getCode());
        existingStudent.getUser().setName(dto.getName());
        existingStudent.getUser().setEmail(dto.getEmail());
        existingStudent.setYear(dto.getYear());
        existingStudent.setPack(pack);

        Student updatedStudent = studentRepository.save(existingStudent);

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