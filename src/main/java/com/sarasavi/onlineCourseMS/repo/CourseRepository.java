package com.sarasavi.onlineCourseMS.repo;

import com.sarasavi.onlineCourseMS.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
}

