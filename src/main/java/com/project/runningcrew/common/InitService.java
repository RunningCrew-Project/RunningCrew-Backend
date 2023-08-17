package com.project.runningcrew.common;


import com.project.runningcrew.area.CSVAreaParser;
import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.entity.GuArea;
import com.project.runningcrew.area.entity.SidoArea;
import com.project.runningcrew.area.repository.DongAreaRepository;
import com.project.runningcrew.area.repository.GuAreaRepository;
import com.project.runningcrew.area.repository.SidoAreaRepository;
import com.project.runningcrew.exception.notFound.DongAreaNotFoundException;
import com.project.runningcrew.exception.notFound.GuAreaNotFoundException;
import com.project.runningcrew.exception.notFound.SidoAreaNotFoundException;
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
    private final CSVAreaParser csvAreaParser;
    private final SidoAreaRepository sidoAreaRepository;
    private final GuAreaRepository guAreaRepository;
    private final DongAreaRepository dongAreaRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        csvAreaParser.createAreas();
        createAdmin();
    }

    private void createAdmin() {
        if (userRepository.existsByEmail("admin@naver.com")) {
            return;
        }

        SidoArea sidoArea = sidoAreaRepository.findByName("서울특별시")
                .orElseThrow(SidoAreaNotFoundException::new);
        GuArea guArea = guAreaRepository.findBySidoAreaAndName(sidoArea, "동대문구")
                .orElseThrow(GuAreaNotFoundException::new);
        DongArea dongArea = dongAreaRepository.findByGuAreaAndName(guArea, "전농2동")
                .orElseThrow(DongAreaNotFoundException::new);
        User user = User.builder()
                .email("admin@naver.com")
                .password(passwordEncoder.encode("admin123!"))
                .name("admin")
                .nickname("admin")
                .imgUrl("https://running-crew-test-s3.s3.ap-northeast-2.amazonaws.com/user/%EC%9C%A0%EC%A0%80%20%EA%B8%B0%EB%B3%B8%20%EC%9D%B4%EB%AF%B8%EC%A7%80.svg")
                .login_type(LoginType.EMAIL)
                .phoneNumber("phoneNumber")
                .dongArea(dongArea)
                .sex(Sex.MAN)
                .birthday(LocalDate.now())
                .height(100)
                .weight(100)
                .build();

        System.out.println("user = " + user);

        userRepository.save(user);
        userRoleRepository.save(new UserRole(user, Role.ADMIN));
    }

}
