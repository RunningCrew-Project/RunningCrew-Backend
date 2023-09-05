package com.project.runningcrew.userrole.entity;

import com.project.runningcrew.exception.notFound.UserRoleNotFoundException;

import java.util.Arrays;

public enum Role {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String value;

    Role(String role) {
        this.value = role;
    }

    public String getValue() {
        return value;
    }

    public static Role getRole(String userRole) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getValue().equals("ROLE_" + userRole))
                .findAny()
                .orElseThrow(UserRoleNotFoundException::new);
    }

}
