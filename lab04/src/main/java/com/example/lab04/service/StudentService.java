package com.example.lab04.service;

import com.example.lab04.Pack;
import com.example.lab04.Student;
import com.example.lab04.dto.StudentRequestDto;
import com.example.lab04.dto.StudentResponseDto;
import com.example.lab04.exception.ResourceNotFoundException;
import com.example.lab04.repository.PackRepository;
import com.example.lab04.repository.StudentRepository;
import org.springframework.http.HttpStatus;
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
    public StudentService(StudentRepository studentRepository, PackRepository packRepository) {
        this.studentRepository = studentRepository;
        this.packRepository = packRepository;
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
    /**
     * MODIFIED 'save' METHOD TO RETURN THE DTO
     */
    @Transactional
    public StudentResponseDto save(StudentRequestDto dto) {
        Pack pack = packRepository.findById(dto.getPackId())
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found with id: " + dto.getPackId()));

        Student student = new Student(
                dto.getCode(),
                dto.getName(),
                dto.getEmail(),
                dto.getYear(),
                pack
        );

        // Save the new entity
        Student savedStudent = studentRepository.save(student);

        // Map the entity to the DTO and return it
        return mapToResponseDto(savedStudent);
    }

    /**
     * NEW HELPER METHOD
     * Maps the internal Entity to the public DTO
     */
    private StudentResponseDto mapToResponseDto(Student student) {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId());
        dto.setCode(student.getCode());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + id));
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
        existingStudent.setName(dto.getName());
        existingStudent.setEmail(dto.getEmail());
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