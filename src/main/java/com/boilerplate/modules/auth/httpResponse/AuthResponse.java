package com.boilerplate.modules.auth.httpResponse;

import com.boilerplate.security.jwt.JwtResponse;
import lombok.*;
import com.boilerplate.modules.user.dto.UserDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private UserDto user;
    private JwtResponse accessToken;
    private JwtResponse refreshToken;
    private String authority;
}
