package com.boilerplate.modules.auth.service;

import com.boilerplate.configuration.exceptions.*;
import com.boilerplate.modules.auth.httpRequest.AuthRequest;
import com.boilerplate.modules.auth.httpResponse.AuthResponse;
import com.boilerplate.modules.user.domain.User;
import com.boilerplate.modules.user.dto.UserDto;
import org.webjars.NotFoundException;

public interface IAuthService {

    UserDto signup(UserDto dto) throws DuplicatedException, InvalidPassword;

    AuthResponse signin(AuthRequest authRequest) throws UserNotFoundException;

    AuthResponse refreshToken() throws TokenExpiredException, NotFoundException;

   /* Boolean forgotPassword(String email) throws UserNotFoundException;

    Long verifyForgotPasswordToken(String token) throws TokenExpiredException, NotFoundException;

    Boolean resetPassword(Long id, String password) throws UserNotFoundException, NotFoundException;*/
}
