package com.sarasavi.onlineCourseMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    private String userName;
    private String password;
    private String role;
}
