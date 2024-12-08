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
@CrossOrigin("*")
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

    // Update Course (only Instructor can access)
    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable Integer courseId, @RequestBody CourseDto course, @RequestHeader(name = "Authorization") String token) {
        if (!jwtAuthenticator.validateJwtToken(token)) {
            return new ResponseEntity<>("Invalid Token!", HttpStatus.FORBIDDEN);
        }
        String role = jwtAuthenticator.getRoleFromToken(token);

        if (!"instructor".equalsIgnoreCase(role)) {
            return new ResponseEntity<>("Access Denied: Only instructors can update courses!", HttpStatus.FORBIDDEN);
        }
        CourseDto courseDto = courseService.updateCourse(courseId, course);
        return new ResponseEntity<>(courseDto, HttpStatus.NO_CONTENT);
    }

    // Delete Course (only Instructor can access)
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> courseDelete(@PathVariable Integer courseId, @RequestHeader(name = "Authorization") String token) {
        if (!jwtAuthenticator.validateJwtToken(token)) {
            return new ResponseEntity<>("Invalid Token!", HttpStatus.FORBIDDEN);
        }
        String role = jwtAuthenticator.getRoleFromToken(token);

        if (!"instructor".equalsIgnoreCase(role)) {
            return new ResponseEntity<>("Access Denied: Only instructors can delete courses!", HttpStatus.FORBIDDEN);
        }
        boolean isDeleted = courseService.deleteCourse(courseId);
        return new ResponseEntity<>(isDeleted, HttpStatus.NO_CONTENT);
    }

    // Add Course with Materials (only Instructor can access)
    @PostMapping("/with-materials")
    public ResponseEntity<Object> addCourseWithMaterials(@RequestBody CourseWithMaterialDto courseWithMaterialDto, @RequestHeader(name = "Authorization") String token) {
        if (!jwtAuthenticator.validateJwtToken(token)) {
            return new ResponseEntity<>("Invalid Token!", HttpStatus.FORBIDDEN);
        }
        String role = jwtAuthenticator.getRoleFromToken(token);

        if (!"instructor".equalsIgnoreCase(role)) {
            return new ResponseEntity<>("Access Denied: Only instructors can add courses with materials!", HttpStatus.FORBIDDEN);
        }
        CourseWithMaterialDto savedCourse = courseService.saveCourseWithMaterials(courseWithMaterialDto);
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
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

    // Update Course with Materials (only Instructor can access)
    @PutMapping("/update-with-materials/{courseId}")
    public ResponseEntity<Object> updateCourseWithMaterials(@PathVariable Integer courseId, @RequestBody CourseWithMaterialDto courseWithMaterialDto, @RequestHeader(name = "Authorization") String token) {
        if (!jwtAuthenticator.validateJwtToken(token)) {
            return new ResponseEntity<>("Invalid Token!", HttpStatus.FORBIDDEN);
        }
        String role = jwtAuthenticator.getRoleFromToken(token);

        if (!"instructor".equalsIgnoreCase(role)) {
            return new ResponseEntity<>("Access Denied: Only instructors can update courses with materials!", HttpStatus.FORBIDDEN);
        }
        CourseWithMaterialDto updatedCourse = courseService.updateCourseWithMaterials(courseId, courseWithMaterialDto);
        return new ResponseEntity<>(updatedCourse, HttpStatus.NO_CONTENT);
    }

    // Delete Course with Materials (only Instructor can access)
    @DeleteMapping("delete-with-materials/{courseId}")
    public ResponseEntity<Object> deleteCourseWithMaterials(@PathVariable Integer courseId, @RequestHeader(name = "Authorization") String token) {
        if (!jwtAuthenticator.validateJwtToken(token)) {
            return new ResponseEntity<>("Invalid Token!", HttpStatus.FORBIDDEN);
        }
        String role = jwtAuthenticator.getRoleFromToken(token);

        if (!"instructor".equalsIgnoreCase(role)) {
            return new ResponseEntity<>("Access Denied: Only instructors can delete courses with materials!", HttpStatus.FORBIDDEN);
        }
        boolean isDeleted = courseService.deleteCourseWithMaterials(courseId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

