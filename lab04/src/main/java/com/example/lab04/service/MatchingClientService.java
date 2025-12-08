package com.example.lab04.service;

import com.example.lab04.*;
import com.example.lab04.dto.solver.*;
import com.example.lab04.repository.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import io.micrometer.core.instrument.Timer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingClientService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PreferenceRepository preferenceRepository;
    private final GradeRepository gradeRepository;
    private final InstructorPreferenceRepository instructorPreferenceRepository;
    private final RestTemplate restTemplate;
    private final AssignmentRepository assignmentRepository;

    private final MeterRegistry meterRegistry;

    private Counter invocationCounter;
    private Timer executionTimer;

    @PostConstruct
    public void initMetrics() {
        invocationCounter = Counter.builder("stable_match.invocations")
                .description("Number of times the StableMatch algorithm was invoked")
                .register(meterRegistry);

        executionTimer = Timer.builder("stable_match.response_time")
                .description("Time taken for StableMatch service to respond")
                .register(meterRegistry);
    }

    @Retry(name = "matchingService")
    @CircuitBreaker(name = "matchingService")
    @TimeLimiter(name = "matchingService", fallbackMethod = "fallbackMatching")
    public CompletableFuture<List<SolverResponseDto>> getMatches(Long packId) {
        log.info("Calculating matches for Pack ID: {}", packId);

        List<Student> students = studentRepository.findAll().stream()
                .filter(s -> s.getPack().getId().equals(packId))
                .peek(s -> s.getUser().getName())
                .collect(Collectors.toList());

        List<Course> courses = courseRepository.findAll().stream()
                .filter(c -> c.getPack().getId().equals(packId) && c.getType() == CourseType.optional)
                .collect(Collectors.toList());


        List<SolverStudentDto> studentDtos = new ArrayList<>();
        for (Student s : students) {
            List<Long> prefs = preferenceRepository.findByStudentId(s.getId()).stream()
                    .sorted(Comparator.comparingInt(Preference::getRank))
                    .map(p -> p.getCourse().getId())
                    .collect(Collectors.toList());
            studentDtos.add(new SolverStudentDto(s.getId(), s.getUser().getName(), prefs));
        }

        List<SolverCourseDto> courseDtos = new ArrayList<>();
        for (Course course : courses) {
            List<Long> rankedStudents = calculateStudentRankingsForCourse(course, students);
            courseDtos.add(new SolverCourseDto(course.getId(), course.getName(), 1, rankedStudents));
        }

        SolverRequestDto request = new SolverRequestDto(studentDtos, courseDtos);

        return CompletableFuture.supplyAsync(() -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<SolverRequestDto> entity = new HttpEntity<>(request, headers);

            String url = "http://localhost:8084/api/solver/match/random";
            log.info("Sending request to StableMatch...");
            invocationCounter.increment();

            Timer.Sample sample = Timer.start(meterRegistry);

            SolverResponseDto[] response = restTemplate.postForObject(url, entity, SolverResponseDto[].class);

            sample.stop(executionTimer);

            List<SolverResponseDto> results = response != null ? Arrays.asList(response) : Collections.emptyList();

            if (!results.isEmpty()) {
                saveAssignmentsToDatabase(results);
            }

            return results;
        });
    }

    private void saveAssignmentsToDatabase(List<SolverResponseDto> results) {
        log.info("Saving {} assignments to database...", results.size());

         assignmentRepository.deleteAll();

        for (SolverResponseDto dto : results) {
            Student student = studentRepository.getReferenceById(dto.getStudentId());
            Course course = courseRepository.getReferenceById(dto.getCourseId());

            Assignment assignment = new Assignment(student, course);

            Optional<Assignment> existing = assignmentRepository.findByStudent_Id(dto.getStudentId());
            if (existing.isPresent()) {
                existing.get().setCourse(course);
                assignmentRepository.save(existing.get());
            } else {
                assignmentRepository.save(assignment);
            }
        }
        log.info("Assignments saved successfully.");
    }

    private List<Long> calculateStudentRankingsForCourse(Course optionalCourse, List<Student> allStudents) {
        List<InstructorPreference> weights = instructorPreferenceRepository.findByOptionalCourse_Id(optionalCourse.getId());

        if (weights.isEmpty()) {
            return allStudents.stream().map(Student::getId).collect(Collectors.toList());
        }

        Map<Student, Double> studentScores = new HashMap<>();

        for (Student student : allStudents) {
            double totalWeightedGrade = 0.0;
            double totalPercentage = 0.0;

            List<Grade> grades = gradeRepository.findByStudent_Code(student.getCode());

            for (InstructorPreference weight : weights) {
                double gradeVal = grades.stream()
                        .filter(g -> g.getCourse().getId().equals(weight.getCompulsoryCourse().getId()))
                        .map(Grade::getValue)
                        .findFirst()
                        .orElse(0.0);

                totalWeightedGrade += gradeVal * weight.getPercentage();
                totalPercentage += weight.getPercentage();
            }

            double finalScore = (totalPercentage > 0) ? (totalWeightedGrade / totalPercentage) : 0.0;
            studentScores.put(student, finalScore);
        }

        return studentScores.entrySet().stream()
                .sorted(Map.Entry.<Student, Double>comparingByValue().reversed())
                .map(entry -> entry.getKey().getId())
                .collect(Collectors.toList());
    }

    public CompletableFuture<List<SolverResponseDto>> fallbackMatching(Long packId, Throwable t) {
        log.error("FAILED to call StableMatch service. Returning empty list. Reason: {}", t.getMessage());
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}