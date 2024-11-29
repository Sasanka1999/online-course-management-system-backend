package com.onlinecoursems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseMaterialDto {
    private Integer id;
    private String fileName;
    private String fileUrl;
    private Integer courseId;
}
