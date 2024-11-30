package com.onlinecoursems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;
    private String fileUrl;

    // Many materials belong to one course
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;
}
