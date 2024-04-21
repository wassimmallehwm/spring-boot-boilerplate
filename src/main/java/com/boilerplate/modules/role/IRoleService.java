package com.boilerplate.modules.role;

import com.boilerplate.modules.base.IBaseService;

import java.util.List;

public interface IRoleService extends IBaseService<Role, RoleDto, Long> {

    RoleDto findByName(String name);

    List<RoleDto> findAll();
}
