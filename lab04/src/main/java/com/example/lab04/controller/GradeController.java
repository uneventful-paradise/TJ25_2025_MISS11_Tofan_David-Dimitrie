package com.example.lab04.controller;

import com.example.lab04.Course;
import com.example.lab04.CourseType;
import com.example.lab04.Grade;
import com.example.lab04.Student;
import com.example.lab04.dto.GradeResponseDto;
import com.example.lab04.repository.CourseRepository;
import com.example.lab04.repository.GradeRepository;
import com.example.lab04.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public GradeController(GradeRepository gradeRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<GradeResponseDto> getAllGrades() {
        return gradeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // 2. Get grades for a specific student
    @GetMapping("/student/{code}")
    public List<GradeResponseDto> getStudentGrades(@PathVariable String code) {
        return gradeRepository.findByStudent_Code(code).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Helper method to map Entity -> DTO
    private GradeResponseDto mapToDto(Grade grade) {
        GradeResponseDto dto = new GradeResponseDto();
        dto.setId(grade.getId());
        dto.setStudentName(grade.getStudent().getUser().getName()); // Access name via User
        dto.setCourseName(grade.getCourse().getName());
        dto.setValue(grade.getValue());
        return dto;
    }

    // 3. Upload CSV
    // CSV Format: student_code,course_code,grade
    @PostMapping("/upload")
    public ResponseEntity<String> uploadGrades(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 3) continue;

                String sCode = data[0].trim();
                String cCode = data[1].trim();
                Double val = Double.parseDouble(data[2].trim());

                // Ideally, move this logic to a Service to avoid duplication with the Listener
                studentRepository.findByCode(sCode).ifPresent(student -> {
                    courseRepository.findByCode(cCode).ifPresent(course -> {
                        if (course.getType() == CourseType.compulsory) {
                            gradeRepository.save(new Grade(student, course, val));
                        }
                    });
                });
                count++;
            }
            return ResponseEntity.ok("Processed " + count + " lines.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}