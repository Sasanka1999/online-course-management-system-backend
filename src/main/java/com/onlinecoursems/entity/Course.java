package com.onlinecoursems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;

    // Many courses can have one instructor
    @ManyToOne
    @JoinColumn(name = "instructor_id", referencedColumnName = "id")
    private User instructor;

    // One course can have many course materials
    @OneToMany(mappedBy = "course")
    private List<CourseMaterial> courseMaterials;

    public Course(Integer courseId) {
        this.id = courseId;
    }
}

