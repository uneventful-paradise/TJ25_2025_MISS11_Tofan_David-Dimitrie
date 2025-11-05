package com.example.lab04.service;

import com.example.lab04.Course;
import com.example.lab04.Instructor;
import com.example.lab04.Pack;
import com.example.lab04.dto.CourseRequestDto;
import com.example.lab04.dto.CourseResponseDto;
import com.example.lab04.exception.ResourceNotFoundException;
import com.example.lab04.repository.CourseRepository;
import com.example.lab04.CourseType;
import com.example.lab04.repository.InstructorRepository;
import com.example.lab04.repository.PackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final PackRepository packRepository;

    public CourseService(CourseRepository courseRepository,
                         InstructorRepository instructorRepository,
                         PackRepository packRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.packRepository = packRepository;
    }

//    public CourseService(CourseRepository courseRepository) {
//        this.courseRepository = courseRepository;
//    }

    @Transactional
    public CourseResponseDto createCourse(CourseRequestDto dto) {
        Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + dto.getInstructorId()));

        Pack pack = packRepository.findById(dto.getPackId())
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found with id: " + dto.getPackId()));

        Course course = new Course(
                dto.getType(),
                dto.getCode(),
                dto.getAbbr(),
                dto.getName(),
                dto.getGroupCount(),
                dto.getDescription(),
                instructor,
                pack
        );

        Course savedCourse = courseRepository.save(course);
        return mapToResponseDto(savedCourse);
    }

    private CourseResponseDto mapToResponseDto(Course course) {
        CourseResponseDto dto = new CourseResponseDto();
        dto.setId(course.getId());
        dto.setType(course.getType());
        dto.setCode(course.getCode());
        dto.setAbbr(course.getAbbr());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setInstructorName(course.getInstructor().getName());
        dto.setPackName(course.getPack().getName());
        return dto;
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }
    @Transactional(readOnly = true)
    public List<CourseResponseDto> findAll() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> findByType(CourseType type) {
        return courseRepository.findByType(type);
    }

    public int updateDescription(String code, String description) {
        return courseRepository.updateCourseDescriptionByCode(code, description);
    }

    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    public long count() {
        return courseRepository.count();
    }

    public void deleteAll() {
        courseRepository.deleteAll();
    }
}
