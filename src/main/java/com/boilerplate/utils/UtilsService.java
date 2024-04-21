package com.boilerplate.utils;

import com.boilerplate.modules.user.domain.User;
import com.boilerplate.modules.user.dto.UserDto;
import com.boilerplate.modules.user.mapper.UserMapper;
import com.boilerplate.modules.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.boilerplate.configuration.ExternalConfigs;
import org.webjars.NotFoundException;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class UtilsService {
    @Autowired
    private ExternalConfigs externalConfigs;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    private final String UPLOAD_DIR = "phishcode";
    private static final String[] HEADERS_TO_TRY = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

    public User getCurrentUser() throws NotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optional = userRepository.findById(user.getId());
        if(optional.isEmpty()){
            throw new NotFoundException("Current user not found ");
        }
        return optional.get();
    }

    public UserDto getCurrentUserDto() throws NotFoundException {
        return userMapper.toDto(getCurrentUser());
    }

    public String getTempDir(String res) {
        if (externalConfigs.isAppEnvDev()) {
            return System.getProperty("java.io.tmpdir") +
                    UPLOAD_DIR + File.separator +
                    res + File.separator;
        }
        return Paths.get(
                        externalConfigs.getRessourceFolderPath() +
                                UPLOAD_DIR + File.separator + res + File.separator
                )
                .toAbsolutePath().normalize().toString();
    }

    public TimeZone extractTimeZone(String clientTZ){
        int timeZone = Integer.parseInt(clientTZ);
        if (timeZone >= 0) {
            clientTZ = "+" + timeZone;
        }
        return TimeZone.getTimeZone("GMT" + clientTZ);
    }

    public String getIPaddress(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
