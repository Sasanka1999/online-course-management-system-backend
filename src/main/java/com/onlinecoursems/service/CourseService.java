package com.onlinecoursems.service;

import com.onlinecoursems.dto.CourseDto;

import java.util.List;

public interface CourseService {
    CourseDto saveCourseWithMaterials(CourseDto courseDto);
    List<CourseDto> getAllCourses();

    CourseDto updateCourse(Integer courseId, CourseDto courseDto);

    boolean deleteCourse(Integer courseId);
}
