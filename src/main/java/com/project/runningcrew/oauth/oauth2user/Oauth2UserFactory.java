package com.project.runningcrew.oauth.oauth2user;

import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import com.project.runningcrew.oauth.oauth2user.parser.AppleUserParser;
import com.project.runningcrew.oauth.oauth2user.parser.GoogleUserParser;
import com.project.runningcrew.oauth.oauth2user.parser.KakaoUserParser;
import com.project.runningcrew.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class Oauth2UserFactory {

    private final KakaoUserParser kakaoUserParser;
    private final GoogleUserParser googleUserParser;
    private final AppleUserParser appleUserParser;

    public User getOauth2User(OauthDto oauthDto) {

        switch (oauthDto.getOrigin()) {
            case "kakao":
                return kakaoUserParser.getKakaoUser(oauthDto);
            case "google":
                return googleUserParser.getGoogleUser(oauthDto);
            case "apple":
                return appleUserParser.getAppleUser(oauthDto);
            default:
                throw new UserNotFoundException();
        }

    }

}
