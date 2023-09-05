package com.project.runningcrew.oauth.oauth2user.parser;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.runningcrew.exception.notFound.UserRoleNotFoundException;
import com.project.runningcrew.oauth.OAuth2User;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import com.project.runningcrew.oauth.oauth2user.userinfo.AppleUserInfo;
import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.userrole.entity.Role;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppleUserParser {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public OAuth2User getAppleUser(OauthDto oauthDto) {

        Map<String, Object> attributes = getAppleAttributes(oauthDto);
        AppleUserInfo appleUserInfo = new AppleUserInfo(attributes);
        String email = appleUserInfo.getEmail();

        Optional<User> optionalUser = userRepository.findByEmailForAdmin(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserRole userRole = userRoleRepository.findByUserForAdmin(user).orElseThrow(UserRoleNotFoundException::new);
            if (user.isDeleted()) {
                userRepository.rollbackUser(user.getId());
                userRoleRepository.rollbackUserRole(userRole.getId());
            }
            return new OAuth2User(user, userRole);
        }

        log.info("입력받은 Email 로 가입된 Apple 회원이 없습니다.");

        User user = User.builder()
                .email(email)
                .login_type(LoginType.APPLE)
                .build();

        User savedUser = userRepository.save(user);
        log.info("Apple 회원등록, user={}", savedUser);

        UserRole userRole = new UserRole(user, Role.USER);
        userRoleRepository.save(userRole);
        log.info("Apple 회원 역할등록, userRole={}", userRole);

        return new OAuth2User(savedUser, userRole);
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
