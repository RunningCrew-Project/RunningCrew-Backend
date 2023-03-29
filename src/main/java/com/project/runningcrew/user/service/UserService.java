package com.project.runningcrew.user.service;


import com.project.runningcrew.exception.notFound.FcmTokenNotFoundException;
import com.project.runningcrew.exception.notFound.RefreshTokenNotFoundException;
import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.exception.duplicate.UserEmailDuplicateException;
import com.project.runningcrew.exception.duplicate.UserNickNameDuplicateException;
import com.project.runningcrew.fcm.token.entity.FcmToken;
import com.project.runningcrew.fcm.token.repository.FcmTokenRepository;

import com.project.runningcrew.image.ImageService;
import com.project.runningcrew.member.entity.Member;
import com.project.runningcrew.member.repository.MemberRepository;
import com.project.runningcrew.member.service.MemberService;
import com.project.runningcrew.notification.repository.NotificationRepository;
import com.project.runningcrew.recruitanswer.repository.RecruitAnswerRepository;
import com.project.runningcrew.refreshtoken.entity.RefreshToken;
import com.project.runningcrew.refreshtoken.repository.RefreshTokenRepository;
import com.project.runningcrew.resourceimage.repository.RunningRecordImageRepository;
import com.project.runningcrew.runningrecord.repository.GpsRepository;
import com.project.runningcrew.runningrecord.repository.RunningRecordRepository;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.userrole.entity.Role;
import com.project.runningcrew.userrole.entity.UserRole;
import com.project.runningcrew.userrole.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final RunningRecordImageRepository runningRecordImageRepository;
    private final RunningRecordRepository runningRecordRepository;
    private final ImageService imageService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRoleRepository userRoleRepository;
    private final NotificationRepository notificationRepository;
    private final RecruitAnswerRepository recruitAnswerRepository;
    private final GpsRepository gpsRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final String imageDirName = "user";

    /**
     * 입력받은 user 를 로그아웃한다. user 의 fcmToken 과 refreshToken 을 삭제한다.
     *
     * @param user 로그아웃 user
     */
    @Transactional
    public void logOut(User user) {
        FcmToken fcmToken = fcmTokenRepository.findByUser(user).
                orElseThrow(FcmTokenNotFoundException::new);
        fcmTokenRepository.delete(fcmToken);

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElseThrow(RefreshTokenNotFoundException::new);
        refreshTokenRepository.delete(refreshToken);
    }

    /**
     * 입력받은 userId 를 가진 User 를 찾아 반환한다. 존재하지 않는다면 UserNotFoundException 을 throw 한다.
     *
     * @param userId 찾는 user 의 id
     * @return 입력받은 userId 를 가진 User
     * @throws UserNotFoundException
     */
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 입력받은 email 을 가진 User 를 찾아 반환한다. 존재하지 않는다면 UserNotFoundException 을 throw 한다.
     *
     * @param email 찾는 User 의 email
     * @return 입력받은 email 로 가입한 User
     * @throws UserNotFoundException
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 입력받은 User 이미지와 User 로 일반 유저를 생성해 저장한 후, User 의 id 를 반환한다.
     *
     * @param user          저장할 user
     * @param multipartFile 저장할 user 의 이미지
     * @return user id
     */
    @Transactional
    public Long saveNormalUser(User user, MultipartFile multipartFile) {

        duplicateEmail(user.getEmail());
        duplicateNickname(user.getNickname());

        String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
        user.updateImgUrl(imageUrl);
        user.updatePassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        userRoleRepository.save(new UserRole(user, Role.USER));

        return savedUser.getId();
    }

    /**
     * 입력받은 User 이미지와 User 로 관리자 유저를 생성해 저장한 후, User 의 id 를 반환한다.
     *
     * @param user          저장할 user
     * @param multipartFile 저장할 user 의 이미지
     * @return user id
     */
    @Transactional
    public Long saveAdminUser(User user, MultipartFile multipartFile) {

        duplicateEmail(user.getEmail());
        duplicateNickname(user.getNickname());

        String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
        user.updateImgUrl(imageUrl);
        user.updatePassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        userRoleRepository.save(new UserRole(user, Role.ADMIN));

        return savedUser.getId();
    }

    /**
     * 변경된 User 정보를 확인하고 변경한다.
     *
     * @param originUser    기존의 user
     * @param newUser       변경될 user
     * @param multipartFile 변경될 user 의 이미지
     */
    @Transactional
    public void updateUser(User originUser, User newUser, MultipartFile multipartFile) {

        //note NotNull
        if (!originUser.getNickname().equals(newUser.getNickname())) {
            if (userRepository.existsByNickname(newUser.getNickname())) {
                throw new UserNickNameDuplicateException(newUser.getNickname());
            } else {
                originUser.updateNickname(newUser.getNickname());
            }
        }

        //note NotNull
        if (!originUser.getDongArea().equals(newUser.getDongArea())) {
            originUser.updateDongArea(newUser.getDongArea());
        }

        //note Nullable
        if(newUser.getHeight() != null) {
            originUser.updateHeight(newUser.getHeight());
        }

        //note Nullable
        if (newUser.getWeight() != null) {
            originUser.updateWeight(newUser.getWeight());
        }

        //note Nullable
        if (newUser.getBirthday() != null) {
            originUser.updateBirthday(newUser.getBirthday());
        }

        //note Nullable
        if (newUser.getSex() != null) {
            originUser.updateSex(newUser.getSex());
        }

        //note NotNull
        if (!multipartFile.isEmpty()) {
            imageService.deleteImage(originUser.getImgUrl());
            String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
            originUser.updateImgUrl(imageUrl);
        }
    }

    /**
     * 입력된 User 와 그에 매핑된 userImg, RunningRecord 를 삭제한다.
     *
     * @param user 삭제할 user
     */
    @Transactional
    public void deleteUser(User user) {
        logOut(user);
        runningRecordImageRepository.deleteAllByUser(user);
        gpsRepository.deleteAllByUser(user);
        runningRecordRepository.deleteAllByUser(user);
        notificationRepository.deleteAllByUser(user);
        recruitAnswerRepository.deleteAllByUser(user);

        List<Member> members = memberRepository.findAllByUser(user);
        for (Member member : members) {
            memberService.deleteMember(member);
        }

        userRoleRepository.deleteByUser(user);
        
        imageService.deleteImage(user.getImgUrl());
        userRepository.delete(user);
    }

    /**
     * 입력받은 email 로 가입된 계정이 있는지 확인한다.
     *
     * @param email 확인할 email
     */
    public boolean duplicateEmail(String email) {

        if(userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicateException(email);
        } else {
            return false;
        }

    }

    /**
     * 입력받은 nickname 으로 가입된 계정이 있는지 확인한다.
     *
     * @param nickname 확인할 nickname
     */
    public boolean duplicateNickname(String nickname) {

        if(userRepository.existsByNickname(nickname)) {
            throw new UserNickNameDuplicateException(nickname);
        } else {
            return false;
        }

    }

}
