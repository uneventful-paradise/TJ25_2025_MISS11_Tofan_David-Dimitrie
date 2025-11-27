package com.example.lab04.service;

import com.example.lab04.Course;
import com.example.lab04.CourseType;
import com.example.lab04.InstructorPreference;
import com.example.lab04.dto.InstructorPreferenceRequestDto;
import com.example.lab04.dto.InstructorPreferenceResponseDto;
import com.example.lab04.exception.ResourceNotFoundException;
import com.example.lab04.repository.CourseRepository;
import com.example.lab04.repository.InstructorPreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstructorPreferenceService {
    private final InstructorPreferenceRepository preferenceRepository;
    private final CourseRepository courseRepository;

    public InstructorPreferenceService(InstructorPreferenceRepository preferenceRepository, CourseRepository courseRepository) {
        this.preferenceRepository = preferenceRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public InstructorPreferenceResponseDto savePreference(InstructorPreferenceRequestDto dto) {
        Course optionalCourse = courseRepository.findById(dto.getOptionalCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Optional Course not found: " + dto.getOptionalCourseId()));

        Course compulsoryCourse = courseRepository.findById(dto.getCompulsoryCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Compulsory Course not found: " + dto.getCompulsoryCourseId()));


        if (optionalCourse.getType() != CourseType.optional) {
            throw new IllegalArgumentException("The first course must be of type OPTIONAL");
        }
        if (compulsoryCourse.getType() != CourseType.compulsory) {
            throw new IllegalArgumentException("The second course must be of type COMPULSORY");
        }

        Optional<InstructorPreference> existingPref = preferenceRepository
                .findByOptionalCourse_IdAndCompulsoryCourse_Id(dto.getOptionalCourseId(), dto.getCompulsoryCourseId());

        InstructorPreference preference;
        if (existingPref.isPresent()) {
            preference = existingPref.get();
            preference.setPercentage(dto.getPercentage()); // Update existing
        } else {
            preference = new InstructorPreference();
            preference.setOptionalCourse(optionalCourse);
            preference.setCompulsoryCourse(compulsoryCourse);
            preference.setPercentage(dto.getPercentage()); // Create new
        }

        InstructorPreference saved = preferenceRepository.save(preference);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<InstructorPreferenceResponseDto> getPreferencesForOptionalCourse(Long optionalCourseId) {
        return preferenceRepository.findByOptionalCourse_Id(optionalCourseId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePreference(Long id) {
        if (!preferenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Preference not found with id: " + id);
        }
        preferenceRepository.deleteById(id);
    }

    private InstructorPreferenceResponseDto mapToDto(InstructorPreference entity) {
        InstructorPreferenceResponseDto dto = new InstructorPreferenceResponseDto();
        dto.setId(entity.getId());
        dto.setOptionalCourseId(entity.getOptionalCourse().getId());
        dto.setOptionalCourseName(entity.getOptionalCourse().getName());
        dto.setCompulsoryCourseId(entity.getCompulsoryCourse().getId());
        dto.setCompulsoryCourseName(entity.getCompulsoryCourse().getName());
        dto.setPercentage(entity.getPercentage());
        return dto;
    }
}