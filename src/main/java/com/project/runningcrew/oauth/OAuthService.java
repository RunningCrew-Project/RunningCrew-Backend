package com.project.runningcrew.oauth;


import com.project.runningcrew.exception.notFound.UserRoleNotFoundException;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import com.project.runningcrew.oauth.dto.response.LoginResponse;
import com.project.runningcrew.oauth.oauth2user.Oauth2UserFactory;
import com.project.runningcrew.security.JwtProvider;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {


    private final JwtProvider jwtProvider;
    private final Oauth2UserFactory oauth2UserFactory;
    private final UserRoleRepository userRoleRepository;


    //note SOCIAL LOGIN
    public LoginResponse socialLogin(OauthDto oauthDto) {

        User user = oauth2UserFactory.getOauth2User(oauthDto);
        UserRole userRole = userRoleRepository.findByUser(user).orElseThrow(UserRoleNotFoundException::new);

        String accessToken = jwtProvider.createAccessToken(user, userRole);
        String refreshToken = jwtProvider.createRefreshToken(user);

        return new LoginResponse(user, accessToken, refreshToken);

    }


}
