package com.boilerplate.modules.auth.service;

import com.boilerplate.modules.role.RolesConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.boilerplate.utils.PasswordHandler;
import com.boilerplate.configuration.ExternalConfigs;
import com.boilerplate.configuration.exceptions.*;
import com.boilerplate.modules.auth.httpRequest.AuthRequest;
import com.boilerplate.modules.auth.httpResponse.AuthResponse;
import com.boilerplate.utils.UtilsService;
import com.boilerplate.modules.role.RoleService;
import com.boilerplate.modules.user.domain.User;
import com.boilerplate.modules.user.dto.UserDto;
import com.boilerplate.modules.user.mapper.UserMapper;
import com.boilerplate.modules.user.repository.UserRepository;
import com.boilerplate.modules.user.service.IUserService;
import com.boilerplate.security.jwt.JwtProvider;
import com.boilerplate.security.jwt.JwtResponse;
import com.boilerplate.utils.email.IEmailService;
import org.webjars.NotFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private IUserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IEmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ExternalConfigs externalConfigs;

    @Override
    @Transactional
    public UserDto signup(UserDto user) throws DuplicatedException, InvalidPassword {
        Optional<User> optionalUser = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
        if (optionalUser.isPresent()) {
            throw new DuplicatedException("User already exists");
        }
        if(!PasswordHandler.isValidPassword(user.getPassword())){
            throw new InvalidPassword("Password must have at least 8 characters, 1 uppercase, 1 lowercase character, 1 number and 1 special character");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleService.findByName(RolesConsts.USER.getRoleName()));
        return userService.create(user);
    }

    @Override
    public AuthResponse signin(AuthRequest authRequest) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmailOrUsername(authRequest.getUsername(), authRequest.getUsername());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Account not found");
        }
        User user = optionalUser.get();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authenticate(user);
    }
    @Override
    public AuthResponse refreshToken() throws TokenExpiredException, NotFoundException {
        return authenticate(utilsService.getCurrentUser());
    }

    /*@Override
    public Boolean forgotPassword(String email) throws UserNotFoundException {
        User user = userService.findByEmail(email.replaceAll("^\"|\"$", ""));
        String activationToken = verifTokenService.create(VerifTokenType.FORGETPASSWORD, user).getValue();
        if(activationToken != null){
            Map<String, String> data = new HashMap<>();
            data.put("activationLink", externalConfigs.getAppLink() + "reset-password/" + activationToken);
            data.put("firstname", user.getFirstname());
            return emailService.sendForgotPasswordEmail(
                    user.getEmail(),
                    data
            );
        }
        return false;
    }
    @Override
    public Long verifyForgotPasswordToken(String token) throws TokenExpiredException, NotFoundException {
        User user = verifTokenService.verify(
                VerifTokenType.FORGETPASSWORD,
                token.replaceAll("^\"|\"$", "")
        );
        return user.getId();
    }

    @Override
    public Boolean resetPassword(Long id, String password) throws UserNotFoundException, NotFoundException {
        UserDto user = userService.findById(id);
        user.setPassword(passwordEncoder.encode(password.replaceAll("^\"|\"$", "")));
        User result = userService.saveDtoToEntity(user, null);
        return result.getId() != null;
    }*/

    private AuthResponse authenticate(User user) {
        UserDto dto = userMapper.toDto(user);
        JwtResponse accessToken = jwtProvider.generateJwtToken(user.getUsername(), false);
        JwtResponse refreshToken = jwtProvider.generateJwtToken(user.getUsername(), true);
        return new AuthResponse(dto, accessToken, refreshToken, user.getRole().getName());
    }

}
