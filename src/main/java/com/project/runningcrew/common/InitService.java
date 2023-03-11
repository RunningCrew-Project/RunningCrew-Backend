package com.project.runningcrew.common;


import com.project.runningcrew.user.entity.LoginType;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.userrole.entity.Role;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InitService implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //TODO 시구동 초기 설정
        createAdmin();
    }

    private void createAdmin() {
        User user = User.builder()
                .email("admin@naver.com")
                .password(passwordEncoder.encode("admin123!"))
                .name("admin")
                .nickname("admin")
                .imgUrl("https://running-crew-s3.s3.ap-northeast-2.amazonaws.com/test/aeeb740e-ec8a-495e-a232-627bffcb2940-test.png")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .dongArea(null)
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();
        userRepository.save(user);
        userRoleRepository.save(new UserRole(user, Role.ADMIN));
    }

}