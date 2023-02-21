package com.project.runningcrew.service;

import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.exception.duplicate.UserEmailDuplicateException;
import com.project.runningcrew.exception.duplicate.UserNickNameDuplicateException;
import com.project.runningcrew.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long saveUser(User user) {
        return userRepository.save(user).getId();
    }


    public void duplicateEmail(String email) throws UserEmailDuplicateException {
        List<User> userOfEmailList = userRepository.findAllByEmail(email);
        if(!userOfEmailList.isEmpty()) {
            throw new UserEmailDuplicateException();
        }
    }

    public void duplicateNickname(String nickname) throws UserNickNameDuplicateException {
        List<User> userOfNicknameList = userRepository.findAllByNickname(nickname);
        if (!userOfNicknameList.isEmpty()) {
            throw new UserNickNameDuplicateException();
        }
    }




}
