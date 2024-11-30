package com.sarasavi.onlineCourseMS.service;

import com.sarasavi.onlineCourseMS.dto.CourseDto;
import com.sarasavi.onlineCourseMS.dto.CourseWithMaterialDto;

import java.util.List;

public interface CourseService {
    CourseDto saveCourse(CourseDto courseDto);
    CourseDto getCourseById(Integer courseId);
    List<CourseDto> getAllCourses();
    CourseDto updateCourse(Integer courseId, CourseDto courseDto);
    boolean deleteCourse(Integer courseId);
    CourseWithMaterialDto saveCourseWithMaterials(CourseWithMaterialDto courseWithMaterialDto);
    CourseWithMaterialDto getCourseWithMaterialById(Integer courseId);
    List<CourseWithMaterialDto> getAllCoursesWithMaterials();
    CourseWithMaterialDto updateCourseWithMaterials(Integer courseId, CourseWithMaterialDto courseWithMaterialDto);
    boolean deleteCourseWithMaterials(Integer courseId);
}
