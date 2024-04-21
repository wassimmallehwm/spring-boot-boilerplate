package com.boilerplate.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordReq {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
