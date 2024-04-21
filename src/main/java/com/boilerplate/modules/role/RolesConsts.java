package com.boilerplate.modules.role;

import lombok.ToString;

@ToString
public enum RolesConsts {
    ADMIN ("ROLE_ADMIN"),
    USER ("ROLE_USER");

    private final String roleName;

    RolesConsts(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return this.roleName;
    }
}
