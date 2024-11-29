package com.onlinecoursems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private Integer id;
    private String title;
    private String description;
    private Integer instructorId;
    private List<CourseMaterialDto> courseMaterials;
}
