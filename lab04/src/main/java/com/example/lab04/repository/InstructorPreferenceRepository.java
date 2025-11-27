package com.example.lab04.repository;

import com.example.lab04.InstructorPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorPreferenceRepository extends JpaRepository<InstructorPreference, Long> {

    // Find all weights defined for a specific optional course
    List<InstructorPreference> findByOptionalCourse_Id(Long optionalCourseId);

    // Find a specific pair (useful to check if it already exists before updating)
    Optional<InstructorPreference> findByOptionalCourse_IdAndCompulsoryCourse_Id(Long optionalCourseId, Long compulsoryCourseId);
}