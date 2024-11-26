package com.sarasavi.onlineCourseMS.controller;

import com.sarasavi.onlineCourseMS.entity.User;
import com.sarasavi.onlineCourseMS.repo.UserRepo;
import com.sarasavi.onlineCourseMS.util.JwtAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserRepo userRepo;
    private final JwtAuthenticator jwtAuthenticator;

    @Autowired
    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
        this.jwtAuthenticator = new JwtAuthenticator();
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        String encodedString = Base64.getEncoder().encodeToString(user.getPassword().getBytes());
        User save = userRepo.save(new User(null, user.getUserName(), encodedString, user.getRole()));
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        User userByUserName = userRepo.findUserByUserName(user.getUserName());

        if (userByUserName != null) {
            byte[] decodedBytes = Base64.getDecoder().decode(userByUserName.getPassword());
            String decodedString = new String(decodedBytes);

            if (decodedString.equals(user.getPassword())) {
                String s = jwtAuthenticator.generateJwtToken(userByUserName);
                return new ResponseEntity<>(s, HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Bad Credentials", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("Login Failed", HttpStatus.FORBIDDEN);
    }
}
