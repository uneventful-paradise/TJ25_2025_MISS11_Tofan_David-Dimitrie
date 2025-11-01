package com.example.lab04;

import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "students")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Student extends BaseUser {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private Integer year;
    //students now have associated packs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pack_id")
    private Pack pack;

    public Student(String code, String name, String email, Integer year, Pack pack) {
        super(name, email);
        this.code = code;
        this.year = year;
        this.pack = pack;
    }
}
