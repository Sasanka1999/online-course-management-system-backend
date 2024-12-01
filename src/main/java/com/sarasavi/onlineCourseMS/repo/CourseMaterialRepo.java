package com.sarasavi.onlineCourseMS.repo;

import com.sarasavi.onlineCourseMS.entity.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseMaterialRepo extends JpaRepository<CourseMaterial, Integer> {
    Optional<CourseMaterial> findByIdAndCourseId(Integer materialId, Integer courseId);
    List<CourseMaterial> findByCourseId(Integer courseId);
}

