package com.project.runningcrew.oauth.oauth2user.parser;

import com.project.runningcrew.exception.notFound.UserRoleNotFoundException;
import com.project.runningcrew.oauth.OAuth2User;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import com.project.runningcrew.oauth.oauth2user.userinfo.KakaoUserInfo;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.userrole.entity.Role;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoUserParser {


    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;


    @Transactional
    public OAuth2User getKakaoUser(OauthDto oauthDto) {

        Map<String, Object> attributes = getKakaoAttributes(oauthDto);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
        String email = kakaoUserInfo.getEmail();

        Optional<User> optionalUser = userRepository.findByEmailForAdmin(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserRole userRole = userRoleRepository.findByUserForAdmin(user).orElseThrow(UserRoleNotFoundException::new);
            if (user.isDeleted()) {
                userRoleRepository.deleteForAdmin(userRole);
                userRepository.deleteForAdmin(user);
            } else {
                return new OAuth2User(user, userRole);
            }
        }

        log.info("입력받은 Email 로 가입된 Kakao 회원이 없습니다.");

        User user = User.builder()
                .email(email)
                .login_type(LoginType.KAKAO)
                .build();

        User savedUser = userRepository.save(user);
        log.info("Kakao 회원등록, user={}", savedUser);

        UserRole userRole = new UserRole(user, Role.USER);
        userRoleRepository.save(userRole);
        log.info("Kakao 회원 역할등록, userRole={}", userRole);

        return new OAuth2User(savedUser, userRole);
    }


    public Map<String, Object> getKakaoAttributes(OauthDto oauthDto) {
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(oauthDto.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }


}
