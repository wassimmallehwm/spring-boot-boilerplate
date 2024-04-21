package com.boilerplate.modules.role;

import com.boilerplate.modules.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.boilerplate.modules.role.Role;

import java.util.Optional;


public interface RoleRepository extends BaseRepository<Role, Long> {
	Role findOneByName(String name);
}