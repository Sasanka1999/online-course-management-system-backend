package com.sarasavi.onlineCourseMS.service;

import com.sarasavi.onlineCourseMS.dto.CourseMaterialDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface CourseMaterialService {
    CourseMaterialDto saveMaterial(CourseMaterialDto courseMaterial);
    String uploadCourseMaterial(MultipartFile file, Integer courseId);
    File downloadCourseMaterial(String fileName);
}
