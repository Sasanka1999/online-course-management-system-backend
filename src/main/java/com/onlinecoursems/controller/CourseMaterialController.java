package com.onlinecoursems.controller;

import com.onlinecoursems.dto.CourseMaterialDto;
import com.onlinecoursems.entity.CourseMaterial;
import com.onlinecoursems.service.CourseMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("courseId") Integer courseId) {
        String result = courseMaterialService.uploadCourseMaterial(file, courseId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        File file = courseMaterialService.downloadCourseMaterial(fileName);
        if (file != null && file.exists()) {
            Path path = Paths.get(file.getAbsolutePath());
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
