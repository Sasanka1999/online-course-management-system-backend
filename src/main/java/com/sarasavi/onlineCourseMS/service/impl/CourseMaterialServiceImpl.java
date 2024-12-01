package com.sarasavi.onlineCourseMS.service.impl;

import com.sarasavi.onlineCourseMS.dto.CourseMaterialDto;
import com.sarasavi.onlineCourseMS.entity.Course;
import com.sarasavi.onlineCourseMS.entity.CourseMaterial;
import com.sarasavi.onlineCourseMS.repo.CourseMaterialRepo;
import com.sarasavi.onlineCourseMS.repo.CourseRepo;
import com.sarasavi.onlineCourseMS.service.CourseMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseMaterialServiceImpl implements CourseMaterialService {

    @Value("${upload-dir}")
    private String uploadDir;

    private final CourseMaterialRepo courseMaterialRepo;
    private final CourseRepo courseRepo;

    @Autowired
    public CourseMaterialServiceImpl(CourseMaterialRepo courseMaterialRepo, CourseRepo courseRepo) {
        this.courseMaterialRepo = courseMaterialRepo;
        this.courseRepo = courseRepo;
    }

    @Override
    public CourseMaterialDto saveMaterial(CourseMaterialDto courseMaterial) {
        Course course = courseRepo.findById(courseMaterial.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found for ID: " + courseMaterial.getCourseId()));
        CourseMaterial save = courseMaterialRepo.save(new CourseMaterial(courseMaterial.getId(), courseMaterial.getFileName(), courseMaterial.getFileUrl(), course));
        return new CourseMaterialDto(save.getId(), save.getFileName(), save.getFileUrl(), save.getCourse().getId());
    }

    @Override
    public String uploadCourseMaterial(MultipartFile file, Integer courseId) throws FileAlreadyExistsException {
        try {
            // Create directory if it doesn't exist
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Check if file already exists
            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            File dest = new File(filePath);
            if (dest.exists()) {
                throw new FileAlreadyExistsException("File already exists: " + file.getOriginalFilename());
            }

            // Save file to the server
            file.transferTo(dest);

            // Save file information to the database
            CourseMaterial courseMaterial = new CourseMaterial();
            courseMaterial.setFileName(file.getOriginalFilename());
            courseMaterial.setFileUrl(filePath);
            courseMaterial.setCourse(new Course(courseId)); // Link to Course

            courseMaterialRepo.save(courseMaterial);

            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (FileAlreadyExistsException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    @Override
    public File downloadCourseMaterial(String fileName) {
        File file = new File(uploadDir + File.separator + fileName);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    @Override
    public List<CourseMaterialDto> getAllCourseMaterials(Integer courseId) {
        List<CourseMaterial> materials = courseMaterialRepo.findByCourseId(courseId);

        return materials.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseMaterialDto getCourseMaterialById(Integer courseId, Integer materialId) {
        // Fetch course material based on both courseId and materialId
        CourseMaterial courseMaterial = courseMaterialRepo.findByIdAndCourseId(materialId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course Material not found for ID: " + materialId + " and Course ID: " + courseId));

        return convertToDto(courseMaterial);  // Convert the entity to DTO before returning
    }

    @Override
    public CourseMaterialDto updateCourseMaterial(Integer materialId, CourseMaterialDto courseMaterialDto) {
        CourseMaterial courseMaterial = courseMaterialRepo.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Course Material not found for ID: " + materialId));

        courseMaterial.setFileName(courseMaterialDto.getFileName());
        courseMaterial.setFileUrl(courseMaterialDto.getFileUrl());

        courseMaterialRepo.save(courseMaterial);

        return convertToDto(courseMaterial);
    }

    @Override
    public void deleteCourseMaterial(Integer materialId) {
        CourseMaterial courseMaterial = courseMaterialRepo.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Course Material not found for ID: " + materialId));

        courseMaterialRepo.delete(courseMaterial);
    }

    private CourseMaterialDto convertToDto(CourseMaterial material) {
        return new CourseMaterialDto(
                material.getId(),
                material.getFileName(),
                material.getFileUrl(),
                material.getCourse().getId()
        );
    }
}

