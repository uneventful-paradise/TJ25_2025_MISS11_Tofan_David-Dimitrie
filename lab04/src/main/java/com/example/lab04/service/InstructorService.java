package com.example.lab04.service;

import com.example.lab04.Role;
import com.example.lab04.User;
import com.example.lab04.dto.InstructorResponseDto;
import com.example.lab04.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstructorService {
    private final UserRepository userRepository;

    public InstructorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<InstructorResponseDto> findAll() {
        return userRepository.findAllByRole(Role.ROLE_INSTRUCTOR).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private InstructorResponseDto mapToResponseDto(User user) {
        InstructorResponseDto dto = new InstructorResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}
