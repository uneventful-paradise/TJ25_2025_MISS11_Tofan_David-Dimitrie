package com.example.lab04.controller;

import com.example.lab04.dto.StudentResponseDto;
import com.example.lab04.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import com.example.lab04.service.CustomUserDetailsService; // Import this
import com.example.lab04.service.JwtTokenProvider;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class) // create fake web server for json conversion and validation
@AutoConfigureMockMvc(addFilters = false) // disable filter
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private CourseService courseService;
    @MockBean
    private InstructorService instructorService;
    @MockBean
    private PackService packService;

    @Test
    void getStudentById_Success() throws Exception {
        Long studentId = 1L;
        StudentResponseDto responseDto = new StudentResponseDto();
        responseDto.setId(studentId);
        responseDto.setCode("S100");
        responseDto.setName("Tofan David");
        responseDto.setEmail("student@test.com");
        responseDto.setPackName("Pack 1");

        when(studentService.findByIdDto(studentId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.code").value("S100"))
                .andExpect(jsonPath("$.name").value("Tofan David"));
    }

    @Test
    void deleteStudent_NotFound_Returns404() throws Exception {
        Long nonExistentId = 999L;

        //throw 404
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"))
                .when(studentService).deleteById(nonExistentId);

        mockMvc.perform(delete("/api/students/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}