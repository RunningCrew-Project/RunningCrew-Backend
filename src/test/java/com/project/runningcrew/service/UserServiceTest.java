package com.project.runningcrew.service;

import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.user.entity.Sex;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.exception.duplicate.UserEmailDuplicateException;
import com.project.runningcrew.exception.duplicate.UserNickNameDuplicateException;
import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.runningrecord.repository.RunningRecordRepository;
import com.project.runningcrew.image.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock UserRepository userRepository;
    @Mock MemberRepository memberRepository;
    @Mock RunningRecordRepository runningRecordRepository;
    @Mock MemberService memberService;
    @Mock ImageService imageService;

    @InjectMocks
    com.project.runningcrew.service.UserService userService;



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
    void saveUserTest(@Mock DongArea dongArea, @Mock MultipartFile multipartFile) throws Exception {
        //given
        Long userId = 1L;
        String imgUrl = "userImgUrl";

        User user = User.builder()
                .id(userId)
                .dongArea(dongArea)
                .build();

        when(imageService.uploadImage(multipartFile, "user")).thenReturn(imgUrl);
        when(userRepository.save(user)).thenReturn(user);

        //when
        Long saveId = userService.saveUser(user, multipartFile);

        //then
        assertThat(userId).isEqualTo(saveId);
        assertThat(user.getImgUrl()).isEqualTo(imgUrl);
        verify(imageService, times(1)).uploadImage(multipartFile, "user");
        verify(userRepository, times(1)).save(user);
    }

    @DisplayName("유저 저장하기 테스트 - 닉네임 중복 예외 발생")
    @Test
    void duplicateNicknameTest(@Mock DongArea dongArea, @Mock MultipartFile multipartFile) throws Exception {
        //given
        User user = User.builder()
                .dongArea(dongArea)
                .nickname("only_one_time")
                .build();

        when(userService.duplicateNickname(user.getNickname())).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> userService.saveUser(user, multipartFile))
                .isInstanceOf(UserNickNameDuplicateException.class);
    }

    @DisplayName("유저 저장하기 테스트 - 이메일 중복 예외 발생")
        @Test
        void duplicateEmailTest(@Mock DongArea dongArea, @Mock MultipartFile multipartFile) throws Exception {
            //given
            User user = User.builder()
                    .dongArea(dongArea)
                    .email("only_one_time")
                    .build();

            when(userService.duplicateEmail(user.getEmail())).thenReturn(true);

            //when
            //then
            assertThatThrownBy(() -> userService.saveUser(user, multipartFile))
                    .isInstanceOf(UserEmailDuplicateException.class);
    }

    @DisplayName("유저 변경하기 테스트 - 성공")
    @Test
    void updateUserTest(@Mock DongArea dongArea1, @Mock DongArea dongArea2, @Mock MultipartFile multipartFile) throws Exception {
        //given
        String newUserImgUrl = "newUserImgUrl";

        User originUser = User.builder()
                .dongArea(dongArea1)
                .nickname("before_nickname")
                .imgUrl("originUserImgUrl")
                .birthday(LocalDate.of(1998, 8, 6))
                .height(180)
                .weight(80)
                .sex(Sex.MAN)
                .build();

        User newUser = User.builder()
                .dongArea(dongArea2)
                .nickname("after_nickname")
                .birthday(LocalDate.of(2023, 2, 28))
                .height(170)
                .weight(70)
                .sex(Sex.WOMAN)
                .build();

        when(userRepository.existsByNickname(newUser.getNickname())).thenReturn(false);
        doNothing().when(imageService).deleteImage(originUser.getImgUrl());
        when(imageService.uploadImage(multipartFile, "user")).thenReturn(newUserImgUrl);

        //when
        userService.updateUser(originUser, newUser, multipartFile);

        //then
        assertThat(originUser.getImgUrl()).isEqualTo(newUserImgUrl);
        assertThat(originUser.getNickname()).isEqualTo(newUser.getNickname());
        assertThat(originUser.getDongArea()).isEqualTo(newUser.getDongArea());
        assertThat(originUser.getBirthday()).isEqualTo(newUser.getBirthday());
        assertThat(originUser.getWeight()).isEqualTo(newUser.getWeight());
        assertThat(originUser.getHeight()).isEqualTo(newUser.getHeight());

    }

    @DisplayName("유저 변경하기 테스트 - 닉네임 중복 예외 발생")
    @Test
    void updateUserTest2(@Mock DongArea dongArea1, @Mock DongArea dongArea2, @Mock MultipartFile multipartFile) throws Exception {
        //given
        String newUserImgUrl = "newUserImgUrl";

        User originUser = User.builder()
                .dongArea(dongArea1)
                .nickname("nickname1")
                .imgUrl("originUserImgUrl")
                .birthday(LocalDate.of(1998, 8, 6))
                .height(180)
                .weight(80)
                .sex(Sex.MAN)
                .build();

        User newUser = User.builder()
                .dongArea(dongArea2)
                .nickname("nickname2")
                .birthday(LocalDate.of(2023, 2, 28))
                .height(170)
                .weight(70)
                .sex(Sex.WOMAN)
                .build();

        when(userRepository.existsByNickname(newUser.getNickname())).thenReturn(true);
        //doNothing().when(imageService).deleteImage(originUser.getImgUrl());
        //when(imageService.uploadImage(multipartFile, "user")).thenReturn(newUserImgUrl);

        //when
        //then
        assertThatThrownBy(() -> userService.updateUser(originUser, newUser, multipartFile))
                .isInstanceOf(UserNickNameDuplicateException.class);

    }

    @DisplayName("유저 삭제하기 테스트")
    @Test
    void deleteUserTest() throws Exception {
        //given

        //when

        //then

    }

}