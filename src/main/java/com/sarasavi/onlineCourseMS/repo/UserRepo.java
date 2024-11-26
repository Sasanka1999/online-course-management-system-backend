package com.sarasavi.onlineCourseMS.repo;

import com.sarasavi.onlineCourseMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findUserByUserName(String username);
}
