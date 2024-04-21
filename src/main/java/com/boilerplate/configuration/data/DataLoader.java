package com.boilerplate.configuration.data;

import com.boilerplate.configuration.exceptions.DuplicatedException;
import com.boilerplate.modules.role.IRoleService;
import com.boilerplate.modules.role.RoleDto;
import com.boilerplate.modules.role.RolesConsts;
import com.boilerplate.modules.user.dto.UserDto;
import com.boilerplate.modules.user.service.IUserService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.boilerplate.configuration.ExternalConfigs;

import java.util.Objects;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(DataLoader.class);

    @Autowired
    private ExternalConfigs externalConfigs;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserService userService;

    public void run(ApplicationArguments args) {
        if (!externalConfigs.isDatabaseInitialized()) {
            initData();
            externalConfigs.setIsDatabaseInitialized(true);
        } else {
            logger.info("Data already initialized.");
        }
    }

    private void initData(){
        RoleDto adminRole = createRoles();
        createSuperAdmin(adminRole);
    }

    private void createSuperAdmin(RoleDto adminRole) {
        if(Objects.nonNull(adminRole)){
            UserDto adminUser = new UserDto();
            adminUser.setEmail("wassimmalleh.wm@gmail.com");
            adminUser.setUsername("wassimmalleh.wm@gmail.com");
            adminUser.setFirstname("Wassim");
            adminUser.setLastname("Malleh");
            adminUser.setRole(adminRole);
            adminUser.setPassword(passwordEncoder.encode("password"));
            try {
                userService.create(adminUser);
            } catch (DuplicatedException e) {
                throw new RuntimeException("Creating admin user failed => " + e.getMessage());
            }
        }
    }

    private RoleDto createRoles() {
        RoleDto adminRole = RoleDto.builder()
                .name(RolesConsts.ADMIN.getRoleName())
                .build();
        RoleDto userRole = RoleDto.builder()
                .name(RolesConsts.USER.getRoleName())
                .build();
        try {
            adminRole = roleService.create(adminRole);
            roleService.create(userRole);
            return adminRole;
        } catch (DuplicatedException e) {
            throw new RuntimeException("Creating roles failed => " + e.getMessage());
        }
    }

}
