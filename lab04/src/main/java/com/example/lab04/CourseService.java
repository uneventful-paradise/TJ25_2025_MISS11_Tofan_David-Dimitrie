package com.example.lab04;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course save(Course course) {
        return courseRepository.save(course);
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
