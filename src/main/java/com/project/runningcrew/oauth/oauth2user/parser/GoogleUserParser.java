package com.project.runningcrew.oauth.oauth2user.parser;

import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import com.project.runningcrew.oauth.oauth2user.userinfo.GoogleUserInfo;
import com.project.runningcrew.oauth.oauth2user.userinfo.KakaoUserInfo;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.userrole.entity.Role;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleUserParser {


    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;



    @Transactional
    public User getGoogleUser(OauthDto oauthDto) {

        Map<String, Object> attributes = getGoogleAttributes(oauthDto);
        GoogleUserInfo googleUserInfo = new GoogleUserInfo(attributes);

        String email = googleUserInfo.getEmail();
        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
        String name = googleUserInfo.getName();
        String profile_image = googleUserInfo.getImageUrl();

        if (!userRepository.existsByEmail(email)) {

            User user = User.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .imgUrl(profile_image)
                    .login_type(LoginType.KAKAO)
                    .build();

            User savedUser = userRepository.save(user);


            UserRole userRole = new UserRole(user, Role.USER);
            userRoleRepository.save(userRole);

            return savedUser;

        }

        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }


    public Map<String, Object> getGoogleAttributes (OauthDto oauthDto) {
        return WebClient.create()
                .get()
                .uri("https://oauth2.googleapis.com/tokeninfo?id_token=" + oauthDto.getIdToken())
                .headers(httpHeaders -> httpHeaders.setBearerAuth(oauthDto.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }



}
