package com.boilerplate.modules.auth;

import com.boilerplate.configuration.exceptions.DuplicatedException;
import com.boilerplate.configuration.exceptions.InvalidPassword;
import com.boilerplate.configuration.exceptions.TokenExpiredException;
import com.boilerplate.configuration.exceptions.UserNotFoundException;
import com.boilerplate.modules.auth.httpResponse.SignupResponse;
import com.boilerplate.modules.auth.service.IAuthService;
import com.boilerplate.modules.user.domain.User;
import com.boilerplate.modules.user.dto.UserDto;
import com.boilerplate.utils.UtilsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.boilerplate.configuration.ExternalConfigs;
import com.boilerplate.modules.auth.httpRequest.AuthRequest;
import com.boilerplate.modules.auth.httpResponse.AuthResponse;


@RestController
@RequestMapping(value = "/api/auth")
@AllArgsConstructor
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final IAuthService authService;
    private final ExternalConfigs externalConfigs;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Works");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthRequest authRequest,
            HttpServletResponse res) throws UserNotFoundException {
        AuthResponse authResponse = authService.signin(authRequest);
        setAuthCookies(res, authResponse);
        return ResponseEntity.ok(authResponse.getUser());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserDto user) throws DuplicatedException, InvalidPassword {
        UserDto userResponse = authService.signup(user);
        SignupResponse signupResponse = new SignupResponse();
        signupResponse.setSuccess(true);
        /*signupResponse.setEmailSent(authService.sendAccountActivation(userResponse));*/
        return ResponseEntity.ok(signupResponse);
    }

    /*@PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody String email) {
        Boolean response = authService.forgotPassword(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/forgot-password-token/{token}")
    public ResponseEntity<?> forgotPasswordToken(@PathVariable String token) {
        Long response = authService.verifyForgotPasswordToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password/{id}")
    public ResponseEntity<?> resetPassword(
            @PathVariable Long id,
            @Valid @RequestBody String password
    ) {
        Boolean response = authService.resetPassword(id, password);
        return ResponseEntity.ok(response);
    }*/

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletResponse res) throws TokenExpiredException {
        AuthResponse authResponse = authService.refreshToken();
        res.setHeader("Access-Control-Allow-Credentials", "true");
        setAuthCookies(res, authResponse);
        return ResponseEntity.ok(true);
    }

    private Cookie accessTokenCookie(AuthResponse authResponse){
        return setCookie(
                "accessToken",
                authResponse.getAccessToken().getToken(),
                authResponse.getAccessToken().getExpiration()
        );
    }
    private Cookie refreshTokenCookie(AuthResponse authResponse){
        return setCookie(
                "refreshToken",
                authResponse.getRefreshToken().getToken(),
                authResponse.getRefreshToken().getExpiration()
        );
    }
    private Cookie setCookie(String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setSecure(externalConfigs.isAppEnvProd());
        cookie.setHttpOnly(true);
        //cookie.setDomain(externalConfigs.getAppLink().substring(0, externalConfigs.getAppLink().length() - 1));
        return cookie;
    }
    private void setAuthCookies(HttpServletResponse response, AuthResponse authResponse){
        Cookie accessTokenCookie = accessTokenCookie(authResponse);
        Cookie refreshTokenCookie = refreshTokenCookie(authResponse);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}
