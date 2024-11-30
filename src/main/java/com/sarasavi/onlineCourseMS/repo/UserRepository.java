package com.sarasavi.onlineCourseMS.repo;

import com.sarasavi.onlineCourseMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
