package com.sarasavi.onlineCourseMS.service.impl;

import com.sarasavi.onlineCourseMS.dto.CourseDto;
import com.sarasavi.onlineCourseMS.dto.CourseWithMaterialDto;
import com.sarasavi.onlineCourseMS.dto.CourseMaterialDto;
import com.sarasavi.onlineCourseMS.entity.Course;
import com.sarasavi.onlineCourseMS.entity.CourseMaterial;
import com.sarasavi.onlineCourseMS.entity.User;
import com.sarasavi.onlineCourseMS.repo.CourseMaterialRepo;
import com.sarasavi.onlineCourseMS.repo.CourseRepository;
import com.sarasavi.onlineCourseMS.repo.UserRepository;
import com.sarasavi.onlineCourseMS.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CourseMaterialRepo courseMaterialRepo;

    @Override
    public CourseDto saveCourse(CourseDto course) {
        User instructor = userRepo.findById(course.getInstructorId())
                .orElseThrow(() -> new RuntimeException("instructor not found"));

        Course save = courseRepo.save(new Course(course.getId(), course.getTitle(), course.getDescription(), instructor, new ArrayList<>()));
        return new CourseDto(save.getId(), save.getTitle(), save.getDescription(),save.getInstructor().getId());
    }

    @Override
    public CourseDto getCourseById(Integer courseId) {
        Optional<Course> byId = courseRepo.findById(courseId);
        if (byId.isPresent()) {
            Course course = byId.get();
            return new CourseDto(course.getId(), course.getTitle(), course.getDescription(), course.getInstructor().getId());
        }
        return null;
    }

    @Override
    public List<CourseDto> getAllCourses() {
        List<Course> all = courseRepo.findAll();
        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course course : all) {
            courseDtos.add(new CourseDto(course.getId(), course.getTitle(), course.getDescription(), course.getInstructor().getId()));
        }
        return courseDtos;
    }

    @Override
    public CourseDto updateCourse(Integer courseId, CourseDto courseDto) {
        if (courseRepo.existsById(courseId)) {
            User instructor = userRepo.findById(courseDto.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found with ID: " + courseDto.getInstructorId()));

            Course save = courseRepo.save(new Course(courseId, courseDto.getTitle(), courseDto.getDescription(), instructor, new ArrayList<>()));

            return new CourseDto(save.getId(), save.getTitle(), save.getDescription(), save.getInstructor().getId()
            );
        } else {
            throw new RuntimeException("Course not found with ID: " + courseId);
        }
    }



    @Override
    public boolean deleteCourse(Integer courseId) {
        if (courseRepo.existsById(courseId)) {
            courseRepo.deleteById(courseId);
            return true;
        }
        return false;
    }

    @Override
    public CourseWithMaterialDto saveCourseWithMaterials(CourseWithMaterialDto courseWithMaterialDto) {

        // Step 1: Fetch the Instructor (User) entity by its ID
        User instructor = userRepo.findById(courseWithMaterialDto.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        // Step 2: Fetch CourseMaterial entities using courseMaterialId
        List<CourseMaterial> materials = new ArrayList<>();
        for (CourseMaterialDto dto : courseWithMaterialDto.getCourseMaterials()) {
            CourseMaterial material = new CourseMaterial(null, dto.getFileName(), dto.getFileUrl(), null);
            materials.add(material);
        }

        // Step 3: Create a new Course entity and set its instructor and course materials
        Course course = new Course(null, courseWithMaterialDto.getTitle(), courseWithMaterialDto.getDescription(), instructor, materials);

        // Step 4: Save the Course entity with its related CourseMaterials
        Course savedCourse = courseRepo.save(course);

        // Step 5: Convert saved CourseMaterials to DTO format
        List<CourseMaterialDto> dtoMaterials = new ArrayList<>();
        for (CourseMaterial material : savedCourse.getCourseMaterials()) {
            dtoMaterials.add(new CourseMaterialDto(material.getId(), material.getFileName(), material.getFileUrl(), savedCourse.getId()));
        }

        // Step 6: Return the saved Course along with related CourseMaterials as a DTO
        return new CourseWithMaterialDto(savedCourse.getId(), savedCourse.getTitle(), savedCourse.getDescription(),
                savedCourse.getInstructor().getId(), dtoMaterials);
    }

    @Override
    public CourseWithMaterialDto getCourseWithMaterialById(Integer courseId) {
        Optional<Course> byId = courseRepo.findById(courseId);

        if (byId.isPresent()) {
            Course course = byId.get();

            return new CourseWithMaterialDto(
                    course.getId(),
                    course.getTitle(),
                    course.getDescription(),
                    course.getInstructor().getId(),
                    course.getCourseMaterials().stream()
                            .map(courseMaterial -> new CourseMaterialDto(
                                    courseMaterial.getId(),
                                    courseMaterial.getFileName(),
                                    courseMaterial.getFileUrl(),
                                    courseMaterial.getCourse().getId()
                            ))
                            .toList() // Ensure toList() is placed outside map()
            );
        }

        throw new RuntimeException("Course not found with ID: " + courseId);
    }


    @Override
    public List<CourseWithMaterialDto> getAllCoursesWithMaterials() {
        List<CourseWithMaterialDto> courseWithMaterialDtos = new ArrayList<>();
        List<Course> courses = courseRepo.findAll();
        for (Course course : courses) {
            List<CourseMaterialDto> materialDtos = new ArrayList<>();
            for (CourseMaterial material : course.getCourseMaterials()) {
                materialDtos.add(new CourseMaterialDto(material.getId(), material.getFileName(), material.getFileUrl(), course.getId()));
            }
            courseWithMaterialDtos.add(new CourseWithMaterialDto(course.getId(), course.getTitle(), course.getDescription(), course.getInstructor().getId(), materialDtos));
        }
        return courseWithMaterialDtos;
    }

    @Override
    public CourseWithMaterialDto updateCourseWithMaterials(Integer courseId, CourseWithMaterialDto courseWithMaterialDto) {
        // Step 1: Fetch the existing Course by its ID
        Course existingCourse = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Step 2: Fetch the Instructor (User) by its ID
        User instructor = userRepo.findById(courseWithMaterialDto.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        // Step 3: Update Course properties
        existingCourse.setTitle(courseWithMaterialDto.getTitle());
        existingCourse.setDescription(courseWithMaterialDto.getDescription());
        existingCourse.setInstructor(instructor);

        // Step 4: Update CourseMaterials
        List<CourseMaterial> updatedMaterials = new ArrayList<>();
        for (CourseMaterialDto dto : courseWithMaterialDto.getCourseMaterials()) {
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

        return new CourseWithMaterialDto(updatedCourse.getId(), updatedCourse.getTitle(), updatedCourse.getDescription(),
                updatedCourse.getInstructor().getId(), dtoMaterials);
    }


    @Override
    public boolean deleteCourseWithMaterials(Integer courseId) {
        if (!courseRepo.existsById(courseId)) {
            throw new RuntimeException("Course not found");
        }
        courseRepo.deleteById(courseId);
        return true;
    }

}

