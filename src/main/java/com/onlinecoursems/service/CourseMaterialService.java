package com.onlinecoursems.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface CourseMaterialService {
    String uploadCourseMaterial(MultipartFile file, Integer courseId);
    File downloadCourseMaterial(String fileName);
}
