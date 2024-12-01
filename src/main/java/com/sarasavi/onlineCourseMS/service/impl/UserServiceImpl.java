package com.sarasavi.onlineCourseMS.service.impl;

import com.sarasavi.onlineCourseMS.dto.UserLoginDto;
import com.sarasavi.onlineCourseMS.dto.UserRegisterDto;
import com.sarasavi.onlineCourseMS.dto.UserResponseDto;
import com.sarasavi.onlineCourseMS.dto.UserDto;
import com.sarasavi.onlineCourseMS.entity.User;
import com.sarasavi.onlineCourseMS.repo.UserRepo;
import com.sarasavi.onlineCourseMS.service.UserService;
import com.sarasavi.onlineCourseMS.util.JwtAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final JwtAuthenticator jwtAuthenticator;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, JwtAuthenticator jwtAuthenticator) {
        this.userRepo = userRepo;
        this.jwtAuthenticator = jwtAuthenticator;
    }
    @Override
    public UserResponseDto login(UserLoginDto userLoginRequest) {
        User user = userRepo.findByUserName(userLoginRequest.getUserName());

        if (user != null) {
            byte[] decodedBytes = Base64.getDecoder().decode(user.getPassword());
            String decodedString = new String(decodedBytes);

            if (decodedString.equals(userLoginRequest.getPassword())) {
                String token = jwtAuthenticator.generateJwtToken(user);
                return new UserResponseDto(token, user.getRole());
            } else {
                throw new RuntimeException("Bad Credentials!");
            }
        }
        throw new RuntimeException("No user found!");
    }

    @Override
    public void register(UserRegisterDto userRegisterRequest) {
        String encodedPassword = Base64.getEncoder().encodeToString(userRegisterRequest.getPassword().getBytes());
        User user = new User(null, userRegisterRequest.getUserName(), encodedPassword, userRegisterRequest.getRole());
        userRepo.save(user);
    }

    @Override
    public User updateUser(Integer id, UserDto updateRequest) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        if (updateRequest.getUserName() != null) {
            existingUser.setUserName(updateRequest.getUserName());
        }
        if (updateRequest.getPassword() != null) {
            String encodedPassword = Base64.getEncoder().encodeToString(updateRequest.getPassword().getBytes());
            existingUser.setPassword(encodedPassword);
        }
        if (updateRequest.getRole() != null) {
            existingUser.setRole(updateRequest.getRole());
        }
        return userRepo.save(existingUser);
    }

    @Override
    public void deleteUser(Integer userId) {
        if (userRepo.existsById(userId)) {
            userRepo.deleteById(userId);
        }else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }
}
