package com.project.runningcrew.oauth.oauth2user.userinfo;

import java.util.Map;

public abstract class Oauth2UserInfo {
    //note Oauth 로 받아오는 정보의 틀

    protected Map<String, Object> attributes;
    public Oauth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getEmail();

}
