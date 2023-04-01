package com.project.runningcrew.oauth.oauth2user.userinfo;

import java.util.Map;



public class AppleUserInfo {

    protected Map<String, Object> attributes;

    public AppleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getName() {
        return "Apple";
    }


}
