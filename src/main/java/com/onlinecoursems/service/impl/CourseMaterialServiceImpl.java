package com.onlinecoursems.service.impl;

import com.onlinecoursems.entity.Course;
import com.onlinecoursems.entity.CourseMaterial;
import com.onlinecoursems.repo.CourseMaterialRepository;
import com.onlinecoursems.service.CourseMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@Service
public class CourseMaterialServiceImpl implements CourseMaterialService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final CourseMaterialRepository courseMaterialRepository;

    @Autowired
    public CourseMaterialServiceImpl(CourseMaterialRepository courseMaterialRepository) {
        this.courseMaterialRepository = courseMaterialRepository;
    }

    @Override
    public String uploadCourseMaterial(MultipartFile file, Integer courseId) {
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

            courseMaterialRepository.save(courseMaterial);

            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            return "File upload failed: " + e.getMessage();
        }
    }

    @Override
    public File downloadCourseMaterial(String fileName) {
        File file = new File(uploadDir + File.separator + fileName);
        return file.exists() ? file : null;
    }
}
