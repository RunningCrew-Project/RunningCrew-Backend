package com.project.runningcrew.oauth.oauth2user.userinfo;

import java.util.Map;



public class AppleUserInfo extends Oauth2UserInfo{

    public AppleUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

}
