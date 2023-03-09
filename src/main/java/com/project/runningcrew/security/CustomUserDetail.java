package com.project.runningcrew.security;

import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomUserDetail implements UserDetails, OAuth2User {

    private User user;
    private UserRole userRole;
    private Map<String, Object> attributes;

    public CustomUserDetail(User user, UserRole userRole) {
        this.user = user;
        this.userRole = userRole;
    }

    public CustomUserDetail(User user, UserRole userRole, Map<String, Object> attributes) {
        this.user = user;
        this.userRole = userRole;
        this.attributes = attributes;
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.getRole().getValue()));
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
        return true;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.user.getEmail();
    }

    public User getUser() {
        return user;
    }

    public UserRole getUserRole() {
        return userRole;
    }

}
