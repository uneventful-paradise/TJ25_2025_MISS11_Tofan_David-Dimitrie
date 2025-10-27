package com.example.lab04;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseType type;

    @Column(nullable = false, unique = true)
    private String code;

    private String abbr;

    @Column(nullable = false)
    private String name;

    @Column(name = "group_count")
    private Integer groupCount;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pack_id")
    private Pack pack;

    public Course(CourseType type, String code, String abbr, String name, Integer groupCount, String description, Instructor instructor, Pack pack) {
        this.type = type;
        this.code = code;
        this.abbr = abbr;
        this.name = name;
        this.groupCount = groupCount;
        this.description = description;
        this.instructor = instructor;
        this.pack = pack;
    }
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", type=" + type +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                // Only print IDs to avoid loading the proxy
                ", instructorId=" + (instructor != null ? instructor.getId() : "null") +
                ", packId=" + (pack != null ? pack.getId() : "null") +
                '}';
    }
}