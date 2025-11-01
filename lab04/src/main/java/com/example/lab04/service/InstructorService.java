package com.example.lab04.service;

import com.example.lab04.Instructor;
import com.example.lab04.dto.InstructorResponseDto;
import com.example.lab04.repository.InstructorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Transactional
    public InstructorResponseDto save(Instructor instructor) {
        Instructor savedInstructor = instructorRepository.save(instructor);
        return mapToResponseDto(savedInstructor);
    }

    /**
     * NEW method to find all
     */
    @Transactional(readOnly = true)
    public List<InstructorResponseDto> findAll() {
        return instructorRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // --- Helper DTO Mapper ---
    private InstructorResponseDto mapToResponseDto(Instructor instructor) {
        InstructorResponseDto dto = new InstructorResponseDto();
        dto.setId(instructor.getId());
        dto.setName(instructor.getName());
        dto.setEmail(instructor.getEmail());
        return dto;
    }

    public List<Instructor> findByName(String name) {
        return instructorRepository.findByNameContaining(name);
    }

    public void deleteAll() {
        instructorRepository.deleteAll();
    }
}
