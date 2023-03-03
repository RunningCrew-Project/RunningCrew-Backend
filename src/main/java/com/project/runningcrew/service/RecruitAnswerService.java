package com.project.runningcrew.service;

import com.project.runningcrew.entity.Crew;
import com.project.runningcrew.entity.RecruitAnswer;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.RecruitAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitAnswerService {

    private final RecruitAnswerRepository recruitAnswerRepository;


    /**
     * 작성된 recruitAnswer 값을 받아 저장하고 id 값을 반환한다.
     * @param answer 작성된 recruitAnswer
     * @return 저장된 recruitAnswer 의 id 값
     */
    public Long saveRecruitAnswer(RecruitAnswer answer) {
        return recruitAnswerRepository.save(answer).getId();
    }

    /**
     * 특정 user 가 특정 crew 에 작성한 RecruitAnswer 를 모두 삭제한다.
     * @param user recruitAnswer 의 user
     * @param crew recruitAnswer 의 crew
     */
    public void deleteByUserAndCrew(User user, Crew crew) {
        recruitAnswerRepository.deleteByUserAndCrew(user, crew);
    }

    /**
     * 특정 user 가 작성한 recruitAnswer 를 offset 순서대로 정렬해서 반환한다.
     * @return offset 순서대로 정렬된 recruitAnswer List
     */
    public List<RecruitAnswer> findAllByUserAndCrew(User user, Crew crew) {
        return recruitAnswerRepository.findAllByUserAndCrew(user, crew);
    }

    /**
     * 특정 crew 에 recruitAnswer 를 작성한 user 목록을 반환한다.
     * @return 특정 crew 에 recruitAnswer 를 작성한 user 목록
     */
    public List<User> findUserByCrew(Crew crew)  {
        return recruitAnswerRepository.findUserByCrew(crew);
    }









}
