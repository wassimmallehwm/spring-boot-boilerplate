package com.boilerplate.modules.user.service;

import java.util.List;
import java.util.Set;

import com.boilerplate.modules.base.IBaseService;
import com.boilerplate.modules.user.domain.User;
import com.boilerplate.modules.user.dto.UserDto;
import com.boilerplate.utils.datagrid.domain.GridModel;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends IBaseService<User, UserDto, Long>, UserDetailsService {
	boolean existsByEmail(String email);

	Page<UserDto> getPage(GridModel gridModel);

	/*byte[] export(Long projectId);*/
}
