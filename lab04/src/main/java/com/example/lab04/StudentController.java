package com.example.lab04;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    //dependency injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * CREATE (POST /api/students)
     * @RequestBody tells Spring to deserialize the JSON from the request body
     * into a Student object.
     * @ResponseStatus(HttpStatus.CREATED) returns a 201 status code.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@RequestBody Student student) {
        return studentService.save(student);
    }

    /**
     * READ (GET /api/students)
     * Gets all students.
     */
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

    /**
     * READ (GET /api/students/{id})
     * @PathVariable maps the {id} from the URL to the 'id' method parameter.
     */
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.findById(id);
    }

    /**
     * UPDATE (PUT /api/students/{id})
     * Updates an existing student.
     */
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        return studentService.update(id, studentDetails);
    }

    /**
     * DELETE (DELETE /api/students/{id})
     * @ResponseStatus(HttpStatus.NO_CONTENT) returns a 204 status code.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteById(id);
    }
}