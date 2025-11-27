package com.example.stablematch.service;

import com.example.stablematch.dto.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchingService {

    public List<MatchingResponseDto> solveMatching(MatchingRequestDto request) {
        List<MatchingResponseDto> results = new ArrayList<>();
        Map<Long, Integer> courseCapacities = new HashMap<>();
        Map<Long, String> courseNames = new HashMap<>();

        for (CourseDto course : request.getCourses()) {
            courseCapacities.put(course.getId(), course.getCapacity());
            courseNames.put(course.getId(), course.getName());
        }

        for (StudentDto student : request.getStudents()) {
            boolean assigned = false;

            for (Long courseId : student.getPreferences()) {
                Integer currentCapacity = courseCapacities.getOrDefault(courseId, 0);

                if (currentCapacity > 0) {
                    results.add(new MatchingResponseDto(
                            student.getId(),
                            student.getName(),
                            courseId,
                            courseNames.get(courseId)
                    ));

                    courseCapacities.put(courseId, currentCapacity - 1);
                    assigned = true;
                    break;
                }
            }

            if (!assigned) {
                System.out.println("Student " + student.getName() + " could not be assigned.");
            }
        }

        return results;
    }

    public List<MatchingResponseDto> solveRandomMatching(MatchingRequestDto request) {
        System.out.println("Received request: " + request.getStudents().size() + " students.");
        List<MatchingResponseDto> results = new ArrayList<>();

        // 1. FIX: Actually populate the map!
        Map<Long, Integer> capacities = new HashMap<>();
        for (CourseDto course : request.getCourses()) {
            capacities.put(course.getId(), course.getCapacity());
        }

        // 2. Shuffle students for randomness
        List<StudentDto> students = new ArrayList<>(request.getStudents());
        Collections.shuffle(students);

        for (StudentDto student : students) {
            // Assign to the first available course found
            for (CourseDto course : request.getCourses()) {
                if (capacities.get(course.getId()) > 0) {
                    results.add(new MatchingResponseDto(
                            student.getId(), student.getName(),
                            course.getId(), course.getName()
                    ));
                    capacities.put(course.getId(), capacities.get(course.getId()) - 1);
                    break; // Student assigned, move to next student
                }
            }
        }
        return results;
    }
}