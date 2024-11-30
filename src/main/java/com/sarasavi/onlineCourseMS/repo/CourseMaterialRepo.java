package com.sarasavi.onlineCourseMS.repo;

import com.sarasavi.onlineCourseMS.entity.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseMaterialRepo extends JpaRepository<CourseMaterial, Integer> {
}

