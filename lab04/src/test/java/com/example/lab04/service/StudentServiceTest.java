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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//verifies business logic
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PackRepository packRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private StudentService studentService;

    @Test
    void saveStudent_Success() {
        Long packId = 1L;
        StudentRequestDto requestDto = new StudentRequestDto();
        requestDto.setEmail("test@student.com");
        requestDto.setPassword("password123");
        requestDto.setName("Tofan David");
        requestDto.setCode("S123");
        requestDto.setYear(2);
        requestDto.setPackId(packId);
        requestDto.setGpa(9.5);

        Pack mockPack = new Pack(2, 1, "Pack A");
        mockPack.setId(packId);

        User mockUser = new User("test@student.com", "encodedPwd", "Tofan David", Role.ROLE_STUDENT);
        Student mockStudent = new Student(mockUser, "S123", 2, mockPack);
        mockStudent.setId(10L);
        mockStudent.setGpa(9.5);
        mockStudent.setStatus("PENDING");

        when(packRepository.findById(packId)).thenReturn(Optional.of(mockPack));
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPwd");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(studentRepository.save(any(Student.class))).thenReturn(mockStudent);

        StudentResponseDto result = studentService.save(requestDto);

        assertNotNull(result);
        assertEquals("S123", result.getCode());
        assertEquals("PENDING", result.getStatus());

        verify(userRepository).save(any(User.class));
        verify(studentRepository).save(any(Student.class));

        verify(rabbitTemplate).convertAndSend(
                eq("saga_exchange"),
                eq("verification.request"),
                any(VerificationRequestEvent.class)
        );
    }

    @Test
    void findById_NotFound_ThrowsException() {
        Long invalidId = 99L;
        when(studentRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.findById(invalidId));

        verify(studentRepository).findById(invalidId);
    }
}