package com.project.runningcrew.oauth.oauth2user.userinfo;

import java.util.Map;

public class KakaoUserInfo extends Oauth2UserInfo {

    public KakaoUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("account_email");
    }


}
