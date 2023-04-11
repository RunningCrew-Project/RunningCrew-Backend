package com.project.runningcrew.user.service;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.crew.entity.Crew;
import com.project.runningcrew.fcm.token.entity.FcmToken;
import com.project.runningcrew.fcm.token.repository.FcmTokenRepository;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.entity.MemberRole;
import com.project.runningcrew.refreshtoken.entity.RefreshToken;
import com.project.runningcrew.refreshtoken.repository.RefreshTokenRepository;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.exception.duplicate.UserEmailDuplicateException;
import com.project.runningcrew.exception.duplicate.UserNickNameDuplicateException;
import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.runningrecord.repository.RunningRecordRepository;
import com.project.runningcrew.image.ImageService;
import com.project.runningcrew.userrole.entity.Role;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    RunningRecordRepository runningRecordRepository;

    @Mock
    UserRoleRepository userRoleRepository;

    @Mock
    FcmTokenRepository fcmTokenRepository;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    ImageService imageService;

    @Mock
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    UserService userService;

    @DisplayName("아이디로 유저 가져오기 테스트 - 성공")
    @Test
    void findByIdTest() throws Exception {
        //given
        Long userId = 1L;
        User user = User.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        User findUser = userService.findById(userId);

        //then
        assertThat(findUser).isEqualTo(user);
        verify(userRepository, times(1)).findById(userId);
    }

    @DisplayName("아이디로 유저 가져오기 테스트 - 예외발생")
    @Test
    void findByIdTest2() throws Exception {
        //given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, times(1)).findById(userId);
    }

    @DisplayName("이메일로 유저 가져오기 테스트 - 성공")
    @Test
    void findByEmailTest() throws Exception {
        //given
        String userEmail = "email";
        User user = User.builder().build();
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        //when
        User findUser = userService.findByEmail(userEmail);

        //then
        assertThat(findUser).isEqualTo(user);
    }

    @DisplayName("이메일로 유저 가져오기 테스트 - 예외발생")
    @Test
    void findByEmailTest2() throws Exception {
        //given
        String userEmail = "email";
        User user = User.builder().build();
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> userService.findByEmail(userEmail))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("유저 저장하기 테스트 - 성공")
    @Test
    void saveNormalUserTest(@Mock DongArea dongArea) throws Exception {
        //given
        Long userId = 1L;
        String imgUrl = "userImgUrl";
        String password = "qwer1234!";
        User user = User.builder()
                .id(userId)
                .password(passwordEncoder.encode(password))
                .dongArea(dongArea)
                .build();

        //when(imageService.uploadImage(multipartFile, "user")).thenReturn(imgUrl);
        when(userRepository.save(user)).thenReturn(user);
        when(userRoleRepository.save(any())).thenReturn(new UserRole(user, Role.USER));

        //when
        Long saveId = userService.saveNormalUser(user);

        //then
        assertThat(userId).isEqualTo(saveId);
        //assertThat(user.getImgUrl()).isEqualTo(imgUrl);
        assertThat(user.getPassword()).isEqualTo(passwordEncoder.encode(password));
        //verify(imageService, times(1)).uploadImage(multipartFile, "user");
        verify(userRepository, times(1)).save(user);
        verify(userRoleRepository, times(1)).save(any());
    }

    @DisplayName("유저 저장하기 테스트 - 닉네임 중복 예외 발생")
    @Test
    void duplicateNicknameTest(@Mock DongArea dongArea) throws Exception {
        //given
        User user = User.builder()
                .dongArea(dongArea)
                .nickname("only_one_time")
                .build();

        when(userService.duplicateNickname(user.getNickname())).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> userService.saveNormalUser(user))
                .isInstanceOf(UserNickNameDuplicateException.class);
    }

    @DisplayName("유저 저장하기 테스트 - 이메일 중복 예외 발생")
    @Test
    void duplicateEmailTest(@Mock DongArea dongArea) throws Exception {
        //given
        User user = User.builder()
                .dongArea(dongArea)
                .email("only_one_time")
                .build();

        when(userService.duplicateEmail(user.getEmail())).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> userService.saveNormalUser(user))
                .isInstanceOf(UserEmailDuplicateException.class);
    }

//    @DisplayName("유저 변경하기 테스트 - 성공")
//    @Test
//    void updateUserTest(@Mock DongArea dongArea1, @Mock DongArea dongArea2, @Mock MultipartFile multipartFile) throws Exception {
//        //given
//        String newUserImgUrl = "newUserImgUrl";
//
//        User originUser = User.builder()
//                .dongArea(dongArea1)
//                .nickname("before_nickname")
//                .imgUrl("originUserImgUrl")
//                .birthday(LocalDate.of(1998, 8, 6))
//                .height(180)
//                .weight(80)
//                .sex(Sex.MAN)
//                .build();
//
//        User newUser = User.builder()
//                .dongArea(dongArea2)
//                .nickname("after_nickname")
//                .birthday(LocalDate.of(2023, 2, 28))
//                .height(170)
//                .weight(70)
//                .sex(Sex.WOMAN)
//                .build();
//
//        when(userRepository.existsByNickname(newUser.getNickname())).thenReturn(false);
//        doNothing().when(imageService).deleteImage(originUser.getImgUrl());
//        when(imageService.uploadImage(multipartFile, "user")).thenReturn(newUserImgUrl);
//
//        //when
//        userService.updateUser(originUser, newUser, multipartFile);
//
//        //then
//        assertThat(originUser.getImgUrl()).isEqualTo(newUserImgUrl);
//        assertThat(originUser.getNickname()).isEqualTo(newUser.getNickname());
//        assertThat(originUser.getDongArea()).isEqualTo(newUser.getDongArea());
//        assertThat(originUser.getBirthday()).isEqualTo(newUser.getBirthday());
//        assertThat(originUser.getWeight()).isEqualTo(newUser.getWeight());
//        assertThat(originUser.getHeight()).isEqualTo(newUser.getHeight());
//
//    }

//    @DisplayName("유저 변경하기 테스트 - 닉네임 중복 예외 발생")
//    @Test
//    void updateUserTest2(@Mock DongArea dongArea1, @Mock DongArea dongArea2, @Mock MultipartFile multipartFile) throws Exception {
//        //given
//        String newUserImgUrl = "newUserImgUrl";
//
//        User originUser = User.builder()
//                .dongArea(dongArea1)
//                .nickname("nickname1")
//                .imgUrl("originUserImgUrl")
//                .birthday(LocalDate.of(1998, 8, 6))
//                .height(180)
//                .weight(80)
//                .sex(Sex.MAN)
//                .build();
//
//        User newUser = User.builder()
//                .dongArea(dongArea2)
//                .nickname("nickname2")
//                .birthday(LocalDate.of(2023, 2, 28))
//                .height(170)
//                .weight(70)
//                .sex(Sex.WOMAN)
//                .build();
//
//        when(userRepository.existsByNickname(newUser.getNickname())).thenReturn(true);
//        //doNothing().when(imageService).deleteImage(originUser.getImgUrl());
//        //when(imageService.uploadImage(multipartFile, "user")).thenReturn(newUserImgUrl);
//
//        //when
//        //then
//        assertThatThrownBy(() -> userService.updateUser(originUser, newUser, multipartFile))
//                .isInstanceOf(UserNickNameDuplicateException.class);
//
//    }

    @DisplayName("유저 삭제하기 테스트")
    @Test
    void deleteUserTest(@Mock DongArea dongArea, @Mock Crew crew) throws Exception {
        //given
        User user = User.builder()
                .dongArea(dongArea)
                .nickname("before_nickname")
                .imgUrl("originUserImgUrl")
                .birthday(LocalDate.of(1998, 8, 6))
                .height(180)
                .weight(80)
                .sex(Sex.MAN)
                .build();

        FcmToken fcmToken = new FcmToken(user, "fcmToken");
        RefreshToken refreshToken = new RefreshToken(user, "refreshToken");
        when(fcmTokenRepository.findByUser(user)).thenReturn(Optional.of(fcmToken));
        doNothing().when(fcmTokenRepository).delete(fcmToken);
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(refreshToken));
        doNothing().when(refreshTokenRepository).delete(refreshToken);

        UserRole userRole = new UserRole(user, Role.USER);
        List<Member> memberList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            memberList.add(new Member(user, crew, MemberRole.ROLE_NORMAL));
        }
        doNothing().when(runningRecordRepository).deleteAllByUser(user);
        when(memberRepository.findAllByUser(user)).thenReturn(memberList);
        doNothing().when(memberRepository).delete(any());

        when(userRoleRepository.findByUser(user)).thenReturn(Optional.of(userRole));
        doNothing().when(userRoleRepository).delete(userRole);
        doNothing().when(imageService).deleteImage("originUserImgUrl");
        doNothing().when(userRepository).delete(user);

        //when
        userService.deleteUser(user);

        //then
        verify(fcmTokenRepository, times(1)).findByUser(user);
        verify(fcmTokenRepository, times(1)).delete(fcmToken);
        verify(refreshTokenRepository, times(1)).findByUser(user);
        verify(refreshTokenRepository, times(1)).delete(refreshToken);

        verify(runningRecordRepository, times(1)).deleteAllByUser(user);
        verify(memberRepository, times(1)).findAllByUser(user);
        verify(memberRepository, times(10)).delete(any());
        verify(userRoleRepository, times(1)).findByUser(user);
        verify(userRoleRepository, times(1)).delete(userRole);
        verify(imageService, times(1)).deleteImage("originUserImgUrl");
        verify(userRepository, times(1)).delete(user);
    }

    @DisplayName("로그아웃 테스트")
    @Test
    public void logOutTest(@Mock User user) {
        //given
        FcmToken fcmToken = new FcmToken(user, "fcmToken");
        RefreshToken refreshToken = new RefreshToken(user, "refreshToken");
        when(fcmTokenRepository.findByUser(user)).thenReturn(Optional.of(fcmToken));
        doNothing().when(fcmTokenRepository).delete(fcmToken);
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(refreshToken));
        doNothing().when(refreshTokenRepository).delete(refreshToken);

        ///when
        userService.logOut(user);

        //then
        verify(fcmTokenRepository, times(1)).findByUser(user);
        verify(fcmTokenRepository, times(1)).delete(fcmToken);
        verify(refreshTokenRepository, times(1)).findByUser(user);
        verify(refreshTokenRepository, times(1)).delete(refreshToken);
    }

}