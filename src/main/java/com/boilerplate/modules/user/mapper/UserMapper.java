package com.boilerplate.modules.user.mapper;

import com.boilerplate.modules.role.RolesConsts;
import org.mapstruct.*;
import com.boilerplate.modules.base.BaseMapper;
import com.boilerplate.modules.role.RoleMapper;
import com.boilerplate.modules.user.domain.User;
import com.boilerplate.modules.user.dto.UserDto;

@Mapper(uses = {
        RoleMapper.class
})
public interface UserMapper extends BaseMapper<User, UserDto> {

    @Mapping(target = "displayName", source = ".", qualifiedByName="toDisplayName")
    @Mapping(target = "isAdmin", source = ".", qualifiedByName="toIsAdmin")
    @Mapping(target = "password", ignore = true)
    @Override
    UserDto toDto(User user);

    @Mapping(
            source = "password",
            target = "password",
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Override
    User toEntity(UserDto dto);

    @Named("toIsAdmin")
    default Boolean setIsAdmin(User user) {
        return user.getRole().getName().equals(RolesConsts.ADMIN.getRoleName());
    }
    @Named("toDisplayName")
    default String translateToDisplayName(User user) {
        return user.getFirstname() + " " + user.getLastname();
    }
}
