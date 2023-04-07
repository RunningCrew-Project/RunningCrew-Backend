package com.project.runningcrew.oauth.oauth2user.parser;

import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.exception.notFound.UserRoleNotFoundException;
import com.project.runningcrew.oauth.OAuth2User;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import com.project.runningcrew.oauth.oauth2user.userinfo.GoogleUserInfo;
import com.project.runningcrew.oauth.oauth2user.userinfo.KakaoUserInfo;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.user.service.UserService;
import com.project.runningcrew.userrole.entity.Role;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleUserParser {


    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;



    @Transactional
    public OAuth2User getGoogleUser(OauthDto oauthDto) {

        Map<String, Object> attributes = getGoogleAttributes(oauthDto);
        GoogleUserInfo googleUserInfo = new GoogleUserInfo(attributes);
        String email = googleUserInfo.getEmail();

        if (!userRepository.existsByEmail(email)) {
            log.info("입력받은 Email 로 가입된 Google 회원이 없습니다.");

            User user = User.builder()
                    .email(email)
                    .login_type(LoginType.KAKAO)
                    .build();

            User savedUser = userRepository.save(user);
            log.info("Google 회원등록, user={}", savedUser);

            UserRole userRole = new UserRole(user, Role.USER);
            userRoleRepository.save(userRole);
            log.info("Google 회원 역할등록, userRole={}", userRole);

            return new OAuth2User(savedUser, userRole);
        }

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        UserRole userRole = userRoleRepository.findByUser(user).orElseThrow(UserRoleNotFoundException::new);
        return new OAuth2User(user, userRole);
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
