package com.project.runningcrew.oauth.oauth2user.parser;

import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import com.project.runningcrew.oauth.oauth2user.userinfo.KakaoUserInfo;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.userrole.entity.Role;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoUserParser {


    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;


    @Transactional
    public User getKakaoUser(OauthDto oauthDto) {

        Map<String, Object> attributes = getKakaoAttributes(oauthDto);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);


        String email = kakaoUserInfo.getEmail();
        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
        String name = kakaoUserInfo.getName();
        String profile_image = kakaoUserInfo.getImageUrl();

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


    public Map<String, Object> getKakaoAttributes(OauthDto oauthDto) {
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(oauthDto.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }


}
