package com.example.lab04.repository;

import com.example.lab04.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourse_Id(Long courseId);
    Optional<Assignment> findByStudent_Id(Long studentId);
}