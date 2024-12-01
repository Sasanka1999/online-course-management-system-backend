package com.sarasavi.onlineCourseMS.service;

import com.sarasavi.onlineCourseMS.dto.UserLoginDto;
import com.sarasavi.onlineCourseMS.dto.UserRegisterDto;
import com.sarasavi.onlineCourseMS.dto.UserResponseDto;
import com.sarasavi.onlineCourseMS.dto.UserDto;
import com.sarasavi.onlineCourseMS.entity.User;

public interface UserService {
    UserResponseDto login(UserLoginDto userLoginDto);
    void register(UserRegisterDto userRegisterDto);
    User updateUser(Integer id, UserDto updateRequest); // Update user by ID
    void deleteUser(Integer userId);
}
