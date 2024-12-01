package com.sarasavi.onlineCourseMS.controller;

import com.sarasavi.onlineCourseMS.dto.CourseMaterialDto;
import com.sarasavi.onlineCourseMS.service.CourseMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/course-materials")
public class CourseMaterialController {

    private final CourseMaterialService courseMaterialService;

    @Autowired
    public CourseMaterialController(CourseMaterialService courseMaterialService) {
        this.courseMaterialService = courseMaterialService;
    }

    @PostMapping
    public ResponseEntity<Object> saveMaterial(@RequestBody CourseMaterialDto courseMaterial) {
        CourseMaterialDto courseMaterialDto = courseMaterialService.saveMaterial(courseMaterial);
        return new ResponseEntity<>(courseMaterialDto, HttpStatus.CREATED);
    }

    @PostMapping("/upload/{courseId}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Integer courseId) {
        try {
            String result = courseMaterialService.uploadCourseMaterial(file, courseId);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("File already exists or failed: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            File file = courseMaterialService.downloadCourseMaterial(fileName);

            if (file == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            String contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<List<CourseMaterialDto>> getAllCourseMaterials(@PathVariable Integer courseId) {
        List<CourseMaterialDto> courseMaterials = courseMaterialService.getAllCourseMaterials(courseId);
        return ResponseEntity.ok(courseMaterials);
    }

    @GetMapping("/{courseId}/{materialId}")
    public ResponseEntity<CourseMaterialDto> getCourseMaterialById(@PathVariable Integer courseId, @PathVariable Integer materialId) {
        try {
            CourseMaterialDto courseMaterialDto = courseMaterialService.getCourseMaterialById(courseId, materialId);

            if (courseMaterialDto == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(courseMaterialDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/{materialId}")
    public ResponseEntity<Object> updateCourseMaterial(@PathVariable Integer materialId, @RequestBody CourseMaterialDto courseMaterial) {
        try {
            CourseMaterialDto updatedCourseMaterial = courseMaterialService.updateCourseMaterial(materialId, courseMaterial);
            if (updatedCourseMaterial == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Material not found for ID: " + materialId);
            }
            return ResponseEntity.ok(updatedCourseMaterial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update course material: " + e.getMessage());
        }
    }

    @DeleteMapping("/{materialId}")
    public ResponseEntity<String> deleteCourseMaterial(@PathVariable Integer materialId) {
        try {
            courseMaterialService.deleteCourseMaterial(materialId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Course material deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete course material: " + e.getMessage());
        }
    }
}
