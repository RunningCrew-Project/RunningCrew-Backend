package com.project.runningcrew.oauth.oauth2user.parser;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import com.project.runningcrew.oauth.oauth2user.userinfo.AppleUserInfo;
import com.project.runningcrew.oauth.oauth2user.userinfo.KakaoUserInfo;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.userrole.entity.Role;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppleUserParser {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public User getAppleUser(OauthDto oauthDto) {

        Map<String, Object> attributes = getAppleAttributes(oauthDto);
        AppleUserInfo appleUserInfo = new AppleUserInfo(attributes);

        String email = appleUserInfo.getEmail();
        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());

        if (!userRepository.existsByEmail(email)) {

            User user = User.builder()
                    .email(email)
                    .password(password)
                    .login_type(LoginType.APPLE)
                    .build();

            User savedUser = userRepository.save(user);

            UserRole userRole = new UserRole(user, Role.USER);
            userRoleRepository.save(userRole);


            return savedUser;
        }

        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }


    public Map<String, Object> getAppleAttributes(OauthDto oauthDto) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(oauthDto.getIdToken());
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            Map<String, Object> attributes = jwtClaimsSet.toJSONObject();

            if(!CollectionUtils.isEmpty(attributes)) {
                return attributes;
            }
        } catch (ParseException e) {
            log.error("getAppleAttributes 오류={}", e.getMessage());
        }

        return null;
    }

}
