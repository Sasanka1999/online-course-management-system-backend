package com.onlinecoursems.service;

import com.onlinecoursems.dto.CourseMaterialDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface CourseMaterialService {
    CourseMaterialDto saveMaterial(CourseMaterialDto courseMaterial);
    String uploadCourseMaterial(MultipartFile file, Integer courseId);
    File downloadCourseMaterial(String fileName);
}
