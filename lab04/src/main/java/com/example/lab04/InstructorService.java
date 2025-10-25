package com.example.lab04;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public Instructor save(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    public List<Instructor> findByName(String name) {
        return instructorRepository.findByNameContaining(name);
    }

    public void deleteAll() {
        instructorRepository.deleteAll();
    }
}
