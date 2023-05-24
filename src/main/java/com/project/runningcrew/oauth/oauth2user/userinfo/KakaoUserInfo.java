package com.project.runningcrew.oauth.oauth2user.userinfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class KakaoUserInfo extends Oauth2UserInfo {

    public KakaoUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> kakaoAccount = objectMapper.convertValue(attributes.get("kakao_account"), HashMap.class);
        return (String) kakaoAccount.get("email");
    }

}
