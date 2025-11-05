package com.example.lab04.service;

import com.example.lab04.Course;
import com.example.lab04.Preference;
import com.example.lab04.repository.PreferenceRepository;
import com.example.lab04.Student;
import com.example.lab04.repository.StudentRepository;
import com.example.lab04.repository.CourseRepository;
import com.example.lab04.dto.PreferenceRequestDto;
import com.example.lab04.dto.PreferenceResponseDto;
import com.example.lab04.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public PreferenceService(PreferenceRepository preferenceRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.preferenceRepository = preferenceRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public PreferenceResponseDto createPreference(PreferenceRequestDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + dto.getStudentId()));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + dto.getCourseId()));

        if (student.getPack() == null || !course.getPack().getId().equals(student.getPack().getId())) {
            throw new IllegalArgumentException("Course is not in the student's year/pack.");
        }

        Preference preference = new Preference(student, course, dto.getRank());
        Preference savedPref = preferenceRepository.save(preference);
        return mapToResponseDto(savedPref);
    }

    @Transactional(readOnly = true)
    public PreferenceResponseDto getPreferenceById(Long id) {
        Preference preference = preferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Preference not found with id: " + id));
        return mapToResponseDto(preference);
    }

    @Transactional(readOnly = true)
    public Preference getPreferenceEntityById(Long id) {
        return preferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Preference not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<PreferenceResponseDto> getPreferencesByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        return preferenceRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePreference(Long id) {
        if (!preferenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Preference not found with id: " + id);
        }
        preferenceRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PreferenceResponseDto> findAll() {
        return preferenceRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private PreferenceResponseDto mapToResponseDto(Preference preference) {
        PreferenceResponseDto dto = new PreferenceResponseDto();
        dto.setId(preference.getId());
        dto.setRank(preference.getRank());
        dto.setStudentId(preference.getStudent().getId());
        dto.setStudentName(preference.getStudent().getName());
        dto.setCourseId(preference.getCourse().getId());
        dto.setCourseName(preference.getCourse().getName());
        return dto;
    }
}