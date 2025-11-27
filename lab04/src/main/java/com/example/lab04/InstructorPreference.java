package com.example.lab04;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instructor_preferences")
@Data
@NoArgsConstructor
public class InstructorPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "optional_course_id")
    private Course optionalCourse;

    @ManyToOne(optional = false)
    @JoinColumn(name = "compulsory_course_id")
    private Course compulsoryCourse;

    @Column(nullable = false)
    private Double percentage; // e.g., 50.0 for 50%
}