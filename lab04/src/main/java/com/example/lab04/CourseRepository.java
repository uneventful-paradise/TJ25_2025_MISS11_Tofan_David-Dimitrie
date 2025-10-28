package com.example.lab04;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    //derived query
    List<Course> findByType(CourseType type);

    //transactional modifying query
    @Transactional
    @Modifying
    @Query("UPDATE Course c SET c.description = :description WHERE c.code = :code")
    int updateCourseDescriptionByCode(@Param("code") String code, @Param("description") String description);
}
