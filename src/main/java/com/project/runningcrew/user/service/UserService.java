package com.project.runningcrew.service;


import com.project.runningcrew.exception.LoginFailException;
import com.project.runningcrew.exception.notFound.UserNotFoundException;
import com.project.runningcrew.exception.duplicate.UserEmailDuplicateException;
import com.project.runningcrew.exception.duplicate.UserNickNameDuplicateException;
import com.project.runningcrew.fcm.token.entity.FcmToken;
import com.project.runningcrew.fcm.token.repository.FcmTokenRepository;

import com.project.runningcrew.image.ImageService;
import com.project.runningcrew.runningrecord.entity.RunningRecord;
import com.project.runningcrew.runningrecord.repository.RunningRecordRepository;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final RunningRecordRepository runningRecordRepository;

    private final ImageService imageService;
    private final String imageDirName = "user";






    /**
     * 로그인, 로그아웃
     */


    /**
     * 로그인 정보를 확인한다(email, password, token), 로그인에 성공하면 userId 를 반환한다.
     * @param email 로그인 email
     * @param password 로그인 password
     * @param token 입력받은 fcmToken 값
     * @return userId
     */
    public Long logIn(String email, String password, String token) {

        User loginUser = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        // 유저 존재여부 검증

        if(loginUser.getPassword().equals(password)) {
            // 비밀번호 검증 성공

            if(!fcmTokenRepository.existsByUser(loginUser)) {
                // FcmToken 정보 없는 경우
                FcmToken fcmToken = new FcmToken(loginUser, token);
                fcmTokenRepository.save(fcmToken);
            }
            return loginUser.getId();

        }
        else {
            // 비밀번호 검증 실패
            throw new LoginFailException();
        }

    }


    /**
     * 입력받은 user 를 로그아웃한다. user 의 fcmToken 을 삭제한다.
     * @param user 로그아웃 user
     */
    public void logOut(User user) {
        // Fcm token 삭제
        FcmToken fcmToken = fcmTokenRepository.findByUser(user).get();
        fcmTokenRepository.delete(fcmToken);
    }













    /**
     * 입력받은 userId 를 가진 User 를 찾아 반환한다. 존재하지 않는다면 UserNotFoundException 을 throw 한다.
     * @param userId 찾는 user 의 id
     * @return 입력받은 userId 를 가진 User
     * @throws UserNotFoundException
     */
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 입력받은 email 을 가진 User 를 찾아 반환한다. 존재하지 않는다면 UserNotFoundException 을 throw 한다.
     * @param email 찾는 User 의 email
     * @return 입력받은 email 로 가입한 User
     * @throws UserNotFoundException
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 입력받은 User 이미지와 User 를 저장하고 User 의 id 를 반환한다.
     * @param user 저장할 user
     * @param multipartFile 저장할 user 의 이미지
     * @return user id
     */
    public Long saveUser(User user, MultipartFile multipartFile) {

        if(duplicateEmail(user.getEmail())) {
            throw new UserEmailDuplicateException();
        }
        if(duplicateNickname(user.getNickname())) {
            throw new UserNickNameDuplicateException();
        }
        // 통과 못하면 예외 날아감.

        String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
        user.updateImgUrl(imageUrl);
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    /**
     * 변경된 User 정보를 확인하고 변경한다.
     * @param originUser 기존의 user
     * @param newUser 변경될 user
     * @param multipartFile 변경될 user 의 이미지
     */
    public void updateUser(User originUser, User newUser, MultipartFile multipartFile) {
        if(!originUser.getNickname().equals(newUser.getNickname())) {
            if(duplicateNickname(newUser.getNickname())) {
                throw new UserNickNameDuplicateException();
            } else {
                originUser.updateNickname(newUser.getNickname());
            }
        } // nickname 중복체크 -> 변경
        if(!originUser.getDongArea().equals(newUser.getDongArea())) {
            originUser.updateDongArea(newUser.getDongArea());
        } // dongArea
        if(originUser.getHeight() != newUser.getHeight()) {
            originUser.updateHeight(newUser.getHeight());
        } // height
        if(originUser.getWeight() != newUser.getWeight()) {
            originUser.updateWeight(newUser.getWeight());
        } // weight
        if(!originUser.getBirthday().equals(newUser.getBirthday())) {
            originUser.updateBirthday(newUser.getBirthday());
        } // birthday
        if (!originUser.getSex().equals(newUser.getSex())) {
            originUser.updateSex(newUser.getSex());
        } // sex
        if(!multipartFile.isEmpty()) {
            imageService.deleteImage(originUser.getImgUrl());
            String imageUrl = imageService.uploadImage(multipartFile, imageDirName);
            originUser.updateImgUrl(imageUrl);
        } // userImg
    }


    /**
     * 입력된 User 와 그에 매핑된 userImg, RunningRecord 를 삭제한다.
     * @param user 삭제할 user
     */
    public void deleteUser(User user) {

        // 해당 user 의 모든 runningRecord 삭제
        List<RunningRecord> deleteRunningRecords = runningRecordRepository.findAllByUser(user);
        runningRecordRepository.deleteAll(deleteRunningRecords);

        // 해당 user 의 이미지 삭제
        imageService.deleteImage(user.getImgUrl());

        // user 삭제
        userRepository.delete(user);
    }










    /**
     * 중복 검증 로직
     */


    /**
     * 입력받은 email 로 가입된 계정이 있는지 확인한다.
     * @param email 확인할 email
     */
    public boolean duplicateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 입력받은 nickname 으로 가입된 계정이 있는지 확인한다.
     * @param nickname 확인할 nickname
     */
    public boolean duplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }




}
