package com.boilerplate.modules.user.domain;


import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.*;
import com.boilerplate.modules.base.BaseEntity;
import com.boilerplate.modules.role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(name = "user_id_idx", columnList = "id"),
        @Index(name = "user_email_idx", columnList = "email"),
        @Index(name = "user_username_idx", columnList = "username")
})
public class User extends BaseEntity implements UserDetails {
    @Column(name = "username")
    private String username;
    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;
    @Column(name = "firstname", nullable = false)
    @NotBlank(message = "Firstname is required")
    private String firstname;
    @Column(name = "lastname", nullable = false)
    @NotBlank(message = "Lastname is required")
    private String lastname;
    @Column(name = "email", nullable = false)
    @NotBlank(message = "Email is required")
    @Email
    private String email;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}