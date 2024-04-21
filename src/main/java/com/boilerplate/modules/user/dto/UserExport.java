package com.boilerplate.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserExport {
    private String firstname;
    private String lastname;
    private String email;
    private String groups;
    private String offender;
    private String simulated;
    private String compromised;
}
