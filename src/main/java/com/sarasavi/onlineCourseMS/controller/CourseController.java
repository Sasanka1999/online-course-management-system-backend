package com.sarasavi.onlineCourseMS.controller;

import com.sarasavi.onlineCourseMS.dto.CourseDto;
import com.sarasavi.onlineCourseMS.dto.CourseWithMaterialDto;
import com.sarasavi.onlineCourseMS.service.CourseService;
import com.sarasavi.onlineCourseMS.util.JwtAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final JwtAuthenticator jwtAuthenticator;
    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
        this.jwtAuthenticator = new JwtAuthenticator();
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody CourseDto course, @RequestHeader(name = "Authorization") String token) {
        if (!jwtAuthenticator.validateJwtToken(token)) {
            return new ResponseEntity<>("Invalid Token!", HttpStatus.FORBIDDEN);
        }
        String role = jwtAuthenticator.getRoleFromToken(token);

        if (!"instructor".equalsIgnoreCase(role)) {
            return new ResponseEntity<>("Access Denied: Only instructors can add courses!", HttpStatus.FORBIDDEN);
        }

        CourseDto courseDto = courseService.saveCourse(course);
        return new ResponseEntity<>(courseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Integer courseId) {
        CourseDto courseById = courseService.getCourseById(courseId);
        return new ResponseEntity<>(courseById, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getCourses(){
        List<CourseDto> allCourses = courseService.getAllCourses();
        return new ResponseEntity<>(allCourses, HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Integer courseId, @RequestBody CourseDto course) {
        CourseDto courseDto = courseService.updateCourse(courseId, course);
        return new ResponseEntity<>(courseDto, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Boolean> courseDelete(@PathVariable Integer courseId){
        boolean b = courseService.deleteCourse(courseId);
        return new ResponseEntity<>(b, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/with-materials")
    public ResponseEntity<CourseWithMaterialDto> addCourse(@RequestBody CourseWithMaterialDto courseWithMaterialDto) {
        CourseWithMaterialDto savedCourse = courseService.saveCourseWithMaterials(courseWithMaterialDto);
        return ResponseEntity.ok(savedCourse);
    }

    @GetMapping("/with-materials/{courseId}")
    public ResponseEntity<CourseWithMaterialDto> getCoursewithMaterialsById(@PathVariable Integer courseId) {
        CourseWithMaterialDto courseById = courseService.getCourseWithMaterialById(courseId);
        return new ResponseEntity<>(courseById, HttpStatus.OK);
    }

    @GetMapping("/with-materials")
    public ResponseEntity<List<CourseWithMaterialDto>> getAllCourses() {
        List<CourseWithMaterialDto> courses = courseService.getAllCoursesWithMaterials();
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/update-with-materials/{courseId}")
    public ResponseEntity<CourseWithMaterialDto> updateCourse(@PathVariable Integer courseId, @RequestBody CourseWithMaterialDto courseWithMaterialDto) {
        CourseWithMaterialDto updatedCourse = courseService.updateCourseWithMaterials(courseId, courseWithMaterialDto);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("delete-with-materials/{courseId}")
    public ResponseEntity<Void> deleteCourseWithMatirials(@PathVariable Integer courseId) {
        boolean isDeleted = courseService.deleteCourseWithMaterials(courseId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

