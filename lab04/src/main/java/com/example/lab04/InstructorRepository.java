package com.example.lab04;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    /**
     * Requirement: A query based on a specific JPQL string
     * Finds all instructors whose names contain a given string (case-insensitive).
     */
    @Query("SELECT i FROM Instructor i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Instructor> findByNameContaining(String name);
}
