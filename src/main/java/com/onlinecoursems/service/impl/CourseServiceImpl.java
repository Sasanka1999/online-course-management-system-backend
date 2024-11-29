package com.onlinecoursems.service.impl;

import com.onlinecoursems.dto.CourseDto;
import com.onlinecoursems.dto.CourseMaterialDto;
import com.onlinecoursems.entity.Course;
import com.onlinecoursems.entity.CourseMaterial;
import com.onlinecoursems.entity.User;
import com.onlinecoursems.repo.CourseMaterialRepository;
import com.onlinecoursems.repo.CourseRepository;
import com.onlinecoursems.repo.UserRepository;
import com.onlinecoursems.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CourseMaterialRepository courseMaterialRepo;

    @Override
    public CourseDto saveCourseWithMaterials(CourseDto courseDto) {

        // Step 1: Fetch the Instructor (User) entity by its ID
        User instructor = userRepo.findById(courseDto.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        // Step 2: Fetch CourseMaterial entities using courseMaterialIds
        List<CourseMaterial> materials = new ArrayList<>();
        for (CourseMaterialDto dto : courseDto.getCourseMaterials()) {
            CourseMaterial material = new CourseMaterial(null, dto.getFileName(), dto.getFileUrl(), null);
            materials.add(material);
        }

        // Step 3: Create a new Course entity and set its instructor and course materials
        Course course = new Course(null, courseDto.getTitle(), courseDto.getDescription(), instructor, materials);

        // Step 4: Save the Course entity with its related CourseMaterials
        Course savedCourse = courseRepo.save(course);

        // Step 5: Convert saved CourseMaterials to DTO format
        List<CourseMaterialDto> dtoMaterials = new ArrayList<>();
        for (CourseMaterial material : savedCourse.getCourseMaterials()) {
            dtoMaterials.add(new CourseMaterialDto(material.getId(), material.getFileName(), material.getFileUrl(), savedCourse.getId()));
        }

        // Step 6: Return the saved Course along with related CourseMaterials as a DTO
        return new CourseDto(savedCourse.getId(), savedCourse.getTitle(), savedCourse.getDescription(),
                savedCourse.getInstructor().getId(), dtoMaterials);
    }

    @Override
    public List<CourseDto> getAllCourses() {
        List<CourseDto> courseDtos = new ArrayList<>();
        List<Course> courses = courseRepo.findAll();
        for (Course course : courses) {
            List<CourseMaterialDto> materialDtos = new ArrayList<>();
            for (CourseMaterial material : course.getCourseMaterials()) {
                materialDtos.add(new CourseMaterialDto(material.getId(), material.getFileName(), material.getFileUrl(), course.getId()));
            }
            courseDtos.add(new CourseDto(course.getId(), course.getTitle(), course.getDescription(), course.getInstructor().getId(), materialDtos));
        }
        return courseDtos;
    }

    @Override
    public CourseDto updateCourse(Integer courseId, CourseDto courseDto) {
        // Step 1: Fetch the existing Course by its ID
        Course existingCourse = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Step 2: Fetch the Instructor (User) by its ID
        User instructor = userRepo.findById(courseDto.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        // Step 3: Update Course properties
        existingCourse.setTitle(courseDto.getTitle());
        existingCourse.setDescription(courseDto.getDescription());
        existingCourse.setInstructor(instructor);

        // Step 4: Update CourseMaterials
        List<CourseMaterial> updatedMaterials = new ArrayList<>();
        for (CourseMaterialDto dto : courseDto.getCourseMaterials()) {
            CourseMaterial material = new CourseMaterial(null, dto.getFileName(), dto.getFileUrl(), existingCourse);
            updatedMaterials.add(material);
        }
        existingCourse.setCourseMaterials(updatedMaterials);

        // Step 5: Save updated Course
        Course updatedCourse = courseRepo.save(existingCourse);

        // Step 6: Convert updated Course and CourseMaterials to DTO format
        List<CourseMaterialDto> dtoMaterials = new ArrayList<>();
        for (CourseMaterial material : updatedCourse.getCourseMaterials()) {
            dtoMaterials.add(new CourseMaterialDto(material.getId(), material.getFileName(), material.getFileUrl(), updatedCourse.getId()));
        }

        return new CourseDto(updatedCourse.getId(), updatedCourse.getTitle(), updatedCourse.getDescription(),
                updatedCourse.getInstructor().getId(), dtoMaterials);
    }


    @Override
    public boolean deleteCourse(Integer courseId) {
        if (!courseRepo.existsById(courseId)) {
            throw new RuntimeException("Course not found");
        }
        courseRepo.deleteById(courseId);
        return true;
    }

}

