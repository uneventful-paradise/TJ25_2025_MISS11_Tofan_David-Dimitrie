package com.example.lab04.repository;

import com.example.lab04.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    List<Preference> findByStudentId(Long studentId);

    Optional<Preference> findByStudentIdAndCourseId(Long studentId, Long courseId);
}