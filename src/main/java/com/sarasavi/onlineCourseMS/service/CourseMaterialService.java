package com.sarasavi.onlineCourseMS.service;

import com.sarasavi.onlineCourseMS.dto.CourseMaterialDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

public interface CourseMaterialService {
    CourseMaterialDto saveMaterial(CourseMaterialDto courseMaterial);
    String uploadCourseMaterial(MultipartFile file, Integer courseId) throws FileAlreadyExistsException;
    File downloadCourseMaterial(String fileName);
    List<CourseMaterialDto> getAllCourseMaterials(Integer courseId);
    CourseMaterialDto getCourseMaterialById(Integer courseId, Integer materialId);
    CourseMaterialDto updateCourseMaterial(Integer materialId, CourseMaterialDto courseMaterial);
    boolean deleteCourseMaterial(Integer materialId);
}
