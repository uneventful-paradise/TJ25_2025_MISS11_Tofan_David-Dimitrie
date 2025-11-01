package com.example.lab04.repository;

import com.example.lab04.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    //jpql query
    @Query("SELECT i FROM Instructor i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Instructor> findByNameContaining(String name);
}
