package com.example.lab04.controller;

import com.example.lab04.Student;
import com.example.lab04.dto.StudentRequestDto;
import com.example.lab04.dto.StudentResponseDto;
import com.example.lab04.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    //dependency injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponseDto createStudent(@Valid @RequestBody StudentRequestDto dto) {
        return studentService.save(dto);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

//    @GetMapping("/{id}")
//    public Student getStudentById(@PathVariable Long id) {
//        return studentService.findById(id);
//    }

    @GetMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    public Student getStudentById(@PathVariable Long id) {
        return studentService.findById(id);
    }

    @PutMapping("/{id}")
    public StudentResponseDto updateStudent(@PathVariable Long id,
                                            @Valid @RequestBody StudentRequestDto dto) {
        return studentService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteById(id);
    }
}