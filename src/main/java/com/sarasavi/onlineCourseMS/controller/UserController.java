package com.sarasavi.onlineCourseMS.controller;

import com.sarasavi.onlineCourseMS.dto.UserLoginDto;
import com.sarasavi.onlineCourseMS.dto.UserRegisterDto;
import com.sarasavi.onlineCourseMS.dto.UserResponseDto;
import com.sarasavi.onlineCourseMS.dto.UserDto;
import com.sarasavi.onlineCourseMS.entity.User;
import com.sarasavi.onlineCourseMS.service.UserService;
import com.sarasavi.onlineCourseMS.util.JwtAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final JwtAuthenticator jwtAuthenticator;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.jwtAuthenticator = new JwtAuthenticator();
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDto loginRequest) {
        UserResponseDto response = userService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDto registerRequest,
                                           @RequestHeader(name = "Authorization") String token) {
        // JWT Token එක Validate කරන්න
        if (!jwtAuthenticator.validateJwtToken(token)) {
            return new ResponseEntity<>("Invalid Token!", HttpStatus.FORBIDDEN);
        }

        // Token එකෙන් Role එක ලබා ගැනීම
        String role = jwtAuthenticator.getRoleFromToken(token);

        // Role අනුව Register කිරීම පරික්ෂා කිරීම
        if (role.equals("admin")) {
            // Admin කෙනෙකුට admin ලෙස register කරන්න අවසර නැත
            if (registerRequest.getRole().equals("admin")) {
                return new ResponseEntity<>("Admins cannot register as admins!", HttpStatus.FORBIDDEN);
            }
        } else if (role.equals("instructor")) {
            // Instructor කෙනෙකුට student හැර වෙන කිසිවක් register කරන්න අවසර නැත
            if (!registerRequest.getRole().equals("student")) {
                return new ResponseEntity<>("Instructors can only register as students!", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Unauthorized role to register users!", HttpStatus.FORBIDDEN);
        }

        // User එක register කිරීම
        userService.register(registerRequest);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserDto updateRequest) {
        User updatedUser = userService.updateUser(id, updateRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK); // Return updated user
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }

}

