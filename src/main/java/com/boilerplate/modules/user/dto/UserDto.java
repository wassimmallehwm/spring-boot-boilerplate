package com.boilerplate.modules.user.dto;

import lombok.*;
import com.boilerplate.modules.base.BaseDto;
import com.boilerplate.modules.role.RoleDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto {

	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String displayName;
	private String email;
	private RoleDto role;
	private Boolean isAdmin;

}
