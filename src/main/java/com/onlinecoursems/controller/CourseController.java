package com.onlinecoursems.controller;

import com.onlinecoursems.dto.CourseDto;
import com.onlinecoursems.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDto> addCourse(@RequestBody CourseDto courseDto) {
        CourseDto savedCourse = courseService.saveCourseWithMaterials(courseDto);
        return ResponseEntity.ok(savedCourse);
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Integer courseId, @RequestBody CourseDto courseDto) {
        CourseDto updatedCourse = courseService.updateCourse(courseId, courseDto);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("delete/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer courseId) {
        boolean isDeleted = courseService.deleteCourse(courseId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

