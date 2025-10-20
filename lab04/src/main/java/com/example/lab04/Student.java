package com.example.lab04;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "students")
@Data //getters, setters, toString(), equals(), hashCode()
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Uses database auto-increment
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer year;

    public Student(String code, String name, String email, Integer year) {
        this.code = code;
        this.name = name;
        this.email = email;
        this.year = year;
    }
}
