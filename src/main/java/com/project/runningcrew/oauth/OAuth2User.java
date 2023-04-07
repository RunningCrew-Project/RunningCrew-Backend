package com.project.runningcrew.oauth;

import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OAuth2User {

    private User user;
    private UserRole userRole;

}
